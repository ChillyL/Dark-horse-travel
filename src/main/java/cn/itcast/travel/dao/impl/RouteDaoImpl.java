package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther ChillyLin
 * @date 2020/3/27
 */
public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据cid查询总记录数
     * @param cid
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        //String sql = "select count(*) from tab_route where cid = ?";

        //定义sql模板
        String sql = "SELECT count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();  //条件,queryForObject需要的参数
        //判断参数是否有值
        if (cid != 0){
            sb.append(" and cid = ?");

            params.add(cid);
        }

        if (rname != null && rname.length() > 0){
            sb.append(" and rname like ?");  //模糊查询

            params.add("%" +rname+ "%");
        }

        sql = sb.toString();

        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    /**
     * 根据cid，start,pageSize查询当前页的数据集合
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
//        String sql = "select * from tab_route where cid = ? limit ? , ?";

        //定义sql
        String sql = "select * from tab_route where 1=1 ";

        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();  //参数
        if (cid != 0){
            sb.append(" and cid = ? ");

            params.add(cid);
        }

        if (rname != null && rname.length() > 0){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");  //模糊查询所需
        }

        sb.append(" limit ? , ? ");  //分页

        sql = sb.toString();

        params.add(start);
        params.add(pageSize);
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }
}
