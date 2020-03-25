package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

/**
 * @auther ChillyLin
 * @date 2020/3/19
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();
    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //根据用户名查询用户对象
        User user1 = userDao.findByUsername(user.getUsername());
        //判断
        if(user1 != null){
            return false;
        }

        //设置激活码
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");

        //保存用户信息
        userDao.save(user);

        //激活邮件发送，邮件正文
        String content="<a href='http://localhost:9090/travel/user/activeUser?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";

        MailUtils.sendMail(user.getEmail(),content,"激活邮件");

        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if(user != null){
            //2.调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        }else{
            return false;
        }

    }

    /**
     * 登录
     * @param user
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
