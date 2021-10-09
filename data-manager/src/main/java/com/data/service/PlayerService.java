package com.data.service;

import com.manager.common.core.domain.model.PlayUser;
import com.manager.common.core.domain.model.UserExchange;

import java.util.List;
import java.util.Map;

/**
 * @author marvin 2021/8/20
 */
public interface PlayerService {
    List selectPlayer(PlayUser playUser);

    List selectPlayerCurr(Long uid);

    Integer updatePlayer(PlayUser playUser);

    String getPhone(Long uid);

    Map getBankInfo(Long uid);

    Map getAlipayInfo(Long uid);

    Integer updateBank(UserExchange userExchange);

    Map getRecAndexc(Long uid);

    List getRecharInfo(String uid);

    List getExchangeInfo(String uid);
}
