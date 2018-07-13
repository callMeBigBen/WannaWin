package citiMerchant.mapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class DBHandler {
    @Autowired
    public static OrderMapper orderMapper;
    @Autowired
    public static ItemMapper itemMapper;
    @Autowired
    public static StrategyMapper strategyMapper;

    static final private String resource = "./mapper.xml";
    static private SqlSessionFactory sqlSessionFactory;

    static private String pathname = "./log.txt";
    static private File log_file;
    static private BufferedWriter log_writer;

    static {
        try {

            //init log_file
            log_file = new File(pathname);

            //init sqlsession
            Reader reader = Resources.getResourceAsReader(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("\nfail to read mapper.xml\n");
        }
    }

    /****************************        初始化完成        ****************************/

    /*
     * 用于日志
     */
    static private void log(String method, long time) {
        try {
            log_writer = new BufferedWriter(new FileWriter(log_file));
            log_writer.write("\"" + method + "\" elapsed time: " + (System.currentTimeMillis() - time) + "ms\n");
            log_writer.flush();
            log_writer.close();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("\nfail to log \"" + method + "\"\n");
        }
    }

    /****************************       用于商户后台的接口     ****************************/

    /*
     * 返回一个列表：包含该商家的“优惠商品”，“减免策略”，“历史订单”的数量。
     */
    static public ArrayList<Integer> getAmountByMerchantID(String merchantID) {
        long time = System.currentTimeMillis();
        int itemAmount = itemMapper.getItemAmountByMerchantID(merchantID);
        int stategyAmount = strategyMapper.getStrategyAmountByMerchantID(merchantID);
        int orderAmount = orderMapper.getOrderAmount(merchantID);
        log("getAmountByMerchantID", time);
        List<Integer> amount = Arrays.asList(itemAmount, stategyAmount, orderAmount);
        return new ArrayList(amount);
    }


    /*
     * 用于统计商家的积分流水信息，包含兑入和兑出。
     */
    static public class Record {
        static public int coupon_record(String IN_MerchantID, int IN_intervalDate) {
            Map<String, Object> map = new HashMap<String, Long>();
            map.put("subPolicyId", Object(IN_MerchantID));
            map.put("fetchNum", Object(IN_intervalDate));
            long time = System.currentTimeMillis();
            SqlSession session = sqlSessionFactory.openSession();
            int totalPoints = session.selectOne("citiMerchant.mapper.DBHandler.coupon_record", map);
            session.commit();
            session.close();
            log("STORED PROCEDURE coupon_record", time);
            return totalPoints;
        }
    }


}
