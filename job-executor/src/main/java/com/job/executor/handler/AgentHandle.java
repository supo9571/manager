package com.job.executor.handler;

import com.job.core.handler.annotation.XxlJob;
import com.job.executor.domain.AgentCommission;
import com.job.executor.mapper.AgentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author marvin 2021/9/17
 * 计算 代理 返佣
 */
@Component
@Slf4j
public class AgentHandle {

    @Autowired
    private AgentMapper agentMapper;

    /**
     * 计算 每日 代理分佣
     * @param date
     */
    @XxlJob("agent_day_income")
    public void dayIncome(String date) {

    }

    /**
     * 计算 代理分佣
     */
    @XxlJob("agent_count_income")
    public void agentIncome() {
        Long endTime = System.currentTimeMillis();
        //查询 玩家id 列表
        List<AgentCommission> agentCommissions = agentMapper.selectUids();
        List<AgentCommission> agents = new ArrayList<>();
        agentCommissions.forEach(agentCommission->{
            getAgentMoney(agentCommission,endTime);
            agents.add(agentCommission);
        });
        if(agents.size()>0)
        agentMapper.saveAgentIncome(agents);
    }

    /**
     * 初始化 佣金数据
     */
    private void getAgentMoney(AgentCommission agentCommission,Long endTime){
        Long beginTime = agentCommission.getEndTime();
        Long uid = agentCommission.getUid();
        String channel = agentCommission.getChannel();

        Integer teamNum = getTeam(uid);//团队人数
        Integer subNum = agentMapper.selectSubNum(uid);//直属人数
        Long subRatio = getSubRatio(uid,beginTime,endTime) ;//直属业绩 增量
        Long otherRatio = getOtherRatio(uid,beginTime,endTime) ;//下属业绩 增量

        /**
         * 直属佣金=直属玩家总流水×此代理线的总流水对应的返佣比例
         */
        Long totalRatio = subRatio+otherRatio;
        Integer rebate = agentMapper.selectRebate(totalRatio,channel);//佣金比例
        rebate = rebate==null?0:agentMapper.selectRebate(totalRatio,channel);
        Long subIncome = subRatio/10000*rebate;//直属佣金 增量
        Long otherIncome = getOtherIncome(uid,beginTime,endTime,channel,rebate) ;//下属佣金 增量

        agentCommission.setTeamNum(teamNum);
        agentCommission.setSubNum(subNum);
        agentCommission.setOtherNum(teamNum-subNum);
        agentCommission.setSubRatio(subRatio);
        agentCommission.setSubIncome(subIncome);
        agentCommission.setOtherRatio(otherRatio);
        agentCommission.setOtherIncome(otherIncome);
        agentCommission.setTotalIncome(subIncome+otherIncome);
        agentCommission.setEndTime(endTime);
    }

    /**
     * 计算 团队人数
     */
    private Integer getTeam(Long uid) {
        AtomicReference<Integer> i = new AtomicReference<>(0);
        List<Long> list = agentMapper.selectSubUid(uid);
        if(list!=null){
            list.forEach(id->{
                i.updateAndGet(v -> v + getTeam(id));
            });
            i.set(list.size());
        }
        return i.get();
    }

    /**
     * 计算 直属业绩
     */
    private Long getSubRatio(Long uid,Long beginTime,Long endTime) {
        AtomicReference<Long> i = new AtomicReference<>(0l);
        List<Long> list = agentMapper.selectSubUid(uid);
        if(list!=null){
            list.forEach(id->{
                Long ratio = agentMapper.selectSubRatio(id,beginTime,endTime);
                Long finalRatio = ratio==null?0l:ratio;
                i.updateAndGet(v -> v + finalRatio);
            });
        }
        return i.get();
    }

    /**
     * 计算 下属业绩
     */
    private Long getOtherRatio(Long uid,Long beginTime,Long endTime) {
        AtomicReference<Long> i = new AtomicReference<>(0l);
        List<Long> list = agentMapper.selectSubUid(uid);
        if(list!=null){
            list.forEach(id->{
                Long ratio = getSubRatio(id,beginTime,endTime);
                i.updateAndGet(v -> v + ratio);
            });
        }
        return i.get();
    }


    /**
     * 计算 下属佣金
     * 下属佣金 = ∑直属玩家对应的下属玩家总流水*（此代理线的总流水对应的返佣比例-直属玩家对应的下属玩家总流水对应的返佣比例）
     */
    private Long getOtherIncome(Long uid,Long beginTime,Long endTime,String channel,Integer rebate){
        AtomicReference<Long> i = new AtomicReference<>(0l);
        List<Long> list = agentMapper.selectSubUid(uid);
        if(list!=null){
            list.forEach(id->{
                List<Long> ids = agentMapper.selectSubUid(id);
                if(ids != null){
                    Long subRatio = getSubRatio(id,beginTime,endTime) ;//直属业绩
                    Long otherRatio = getOtherRatio(id,beginTime,endTime) ;//下属业绩
                    Long totalRatio = subRatio+otherRatio;
                    Integer otherRebate = agentMapper.selectRebate(totalRatio,channel);//佣金比例
                    otherRebate = otherRebate==null?0:agentMapper.selectRebate(totalRatio,channel);
                    Long subIncome = subRatio/10000*(rebate-otherRebate);//直属佣金
                    Long otherIncome = getOtherIncome(id,beginTime,endTime,channel,otherRebate);//直属佣金
                    i.updateAndGet(v -> v + subIncome+otherIncome);
                }
            });
        }
        return i.get();
    }
}