package com.manager.openFegin;

import com.manager.common.core.domain.AjaxResult;
import com.manager.common.core.domain.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author marvin 2021/8/19
 */
@FeignClient(value = "data-manager")
public interface DataService {

    @PostMapping(value = "/data/coins/list", consumes = "application/json")
    AjaxResult getCoins(@RequestBody Coins coins);

    @PostMapping(value = "/data/login/list", consumes = "application/json")
    AjaxResult getLogins(@RequestBody Login login);

    @PostMapping(value = "/data/login/today")
    AjaxResult selectTodayLogins();

    @PostMapping(value = "/data/login/count")
    AjaxResult count(@RequestParam("type") String type);

    @PostMapping(value = "/data/online/list", consumes = "application/json")
    AjaxResult getOnlines(@RequestBody OnlinePlayer onlinePlayer);

    @PostMapping(value = "/data/player/list", consumes = "application/json")
    AjaxResult getPlayers(@RequestBody PlayUser playUser);

    @PostMapping(value = "/data/player/curr")
    AjaxResult getPlayerCurr(@RequestParam("uid") Long uid);

    @PostMapping(value = "/data/card/list", consumes = "application/json")
    AjaxResult getCards(@RequestBody Card card);

    @PostMapping(value = "/data/card/info")
    AjaxResult findCardInfo(@RequestParam("tableGid")String tableGid);

    @PostMapping(value = "/data/game/option")
    AjaxResult getGames();

    @PostMapping(value = "/data/player/edit", consumes = "application/json")
    AjaxResult updatePlayer(@RequestBody PlayUser playUser);

    @PostMapping(value = "/data/allupdate/add", consumes = "application/json")
    AjaxResult addAllUpdate(@RequestBody Allupdate allupdate);

    @PostMapping(value = "/data/allupdate/list")
    AjaxResult findAllUpdate(@RequestBody Allupdate allupdate);

    @PostMapping(value = "/data/allupdate/history")
    AjaxResult findAllUpdateHistory(@RequestParam("tid") Integer tid);

    @PostMapping(value = "/data/allupdate/edit", consumes = "application/json")
    AjaxResult editAllUpdateHistory(@RequestBody Allupdate allupdate);

    @PostMapping(value = "/data/allupdate/del")
    AjaxResult deleteAllupdate(@RequestParam("id") Integer id);

    //热更新
    @PostMapping(value = "/data/hotupdate/list")
    AjaxResult findHotupdate(@RequestBody Hotupdate hotupdate);

    @PostMapping(value = "/data/hotupdate/add")
    AjaxResult addHotUpdate(@RequestBody Hotupdate hotUpdate);

    @PostMapping(value = "/data/hotupdate/edit")
    AjaxResult editHotUpdate(@RequestBody Hotupdate hotUpdate);

    @PostMapping(value = "/data/hotupdate/del")
    AjaxResult delHotupdate(@RequestParam("id") Integer id);

    @PostMapping(value = "/data/hotupdate/find")
    AjaxResult findhotUpdateById(@RequestParam("id") Integer id);

}
