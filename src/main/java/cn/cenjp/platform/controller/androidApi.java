package cn.cenjp.platform.controller;import cn.cenjp.platform.bean.Cart;import cn.cenjp.platform.bean.MySpike;import cn.cenjp.platform.bean.User;import cn.cenjp.platform.result.CodeMsg;import cn.cenjp.platform.result.Result;import cn.cenjp.platform.service.AndroidService;import cn.cenjp.platform.service.GoodService;import cn.cenjp.platform.service.UserService;import cn.cenjp.platform.vo.GoodVo;import cn.cenjp.platform.vo.Order;import com.github.pagehelper.PageHelper;import com.github.pagehelper.PageInfo;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Value;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Controller;import org.springframework.ui.Model;import org.springframework.web.bind.annotation.*;import javax.servlet.http.HttpServletResponse;import java.io.FileInputStream;import java.io.IOException;import java.io.OutputStream;import java.lang.reflect.Method;import java.util.List;/** * 为安卓提供的服务器接口 */@RestController@RequestMapping("/Android")public class androidApi{    @Value("${pageSize}")    private Integer pageSize;    @Value("${picture.Location}")    private String picturelocation;    @Autowired    UserService userService;    @Autowired    private PasswordEncoder passwordEncoder;    @Autowired    private GoodService goodService;    @Autowired    private AndroidService androidService;    /**     * 登录接口     * @param phone 用户名     * @param password 密码     * @return 成功返回用户数据     */    @RequestMapping(value = "/login",method = RequestMethod.POST)    public Result<User> doLogin(@RequestParam("phone")String phone, @RequestParam("password")String password){        User user = userService.getUserByPhone(phone);        System.out.println(user.toString());        if (user==null){            return Result.error(CodeMsg.MOBILE_NOT_EXIST);        }        else{            return Result.success(user);        }    }    /**     *分页获取商品接口     * @param goodKind 商品种类     * @param  pageIndex 页码     * @return 返回商品数据     */    @RequestMapping(value = "/getList/{goodKind}/{pageIndex}")    public Result<List<GoodVo>> getGood(@PathVariable("goodKind") String goodKind, @PathVariable("pageIndex") Integer pageIndex){        PageHelper.startPage(pageIndex, pageSize);        List<GoodVo> goodVoList = goodService.getGoodVoList(goodKind);        PageInfo<GoodVo> pageInfo = new PageInfo<>(goodVoList);        List<GoodVo> list = pageInfo.getList();        if (list.isEmpty()){            return Result.error(CodeMsg.GOOD_NO_MORE);        }else{            return Result.success(list);        }    }    /**     * 获得商品详细信息     * @param goodId 商品id     * @return     */    @RequestMapping(value = "/getGood/{goodId}")    public Result<GoodVo> getGoodInfo(@PathVariable("goodId") String goodId){        GoodVo vo = goodService.getGoodVo(goodId);        if (vo==null)            return Result.error(CodeMsg.GOOD_NOT_EXIST);        return Result.success(vo);    }    /**     * 秒杀商品     * @param goodId 商品id     * @param spikeId 秒杀id     * @return 返回订单信息     */    @RequestMapping(value = "/doSpike/{goodId}/{spikeId}")    public Result<Order> doSpilke(@PathVariable("goodId") String goodId, @PathVariable("spikeId") Integer spikeId){        return null;    }    /**     * 获得个人秒杀的商品信息     * @param phone 手机号码     * @return 返回秒杀的商品列表     */    @RequestMapping(value = "/getMySpike/{phone}")    public Result<List<MySpike>> getMySpike(@PathVariable("phone") String phone){        return null;    }    @RequestMapping(value = "/getPageSize/{goodKind}")    public Result<Integer> getPageSize(@PathVariable("goodKind") String goodKind){        int count = (int) androidService.countGood(goodKind);        if (count%pageSize==0){            return Result.success(count/4);        }else{            return Result.success(count/pageSize+1);        }    }    @RequestMapping(value = "/getPicture")    public String getPicture(@RequestParam("name") String picName,                             HttpServletResponse response) {        FileInputStream fis = null;        OutputStream os = null;        System.out.println(picName);        try {            fis = new FileInputStream(picturelocation+picName);            os = response.getOutputStream();            int count = 0;            byte[] buffer = new byte[1024 * 8];            while ((count = fis.read(buffer)) != -1) {                os.write(buffer, 0, count);                os.flush();            }        } catch (Exception e) {            e.printStackTrace();        }        try {            fis.close();            os.close();        } catch (IOException e) {            e.printStackTrace();        }        return "ok";    }}