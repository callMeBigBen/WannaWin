package citi.persist.mapper;

import citi.vo.Merchant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * 接口设计：刘钟博
 * 代码填充：任思远
 */

@Repository
public interface MerchantMapper {

    final String getAllMerchant = "SELECT * FROM merchant";
    final String getAllMerchantID = "SELECT MerchantID FROM merchant";
    final String addMerchant = "INSERT INTO merchant(MerchantID, name, password, description, cardDescription, address, merchantLogoURL, cardLogoURL, proportion, activityTheme, activityDescription, cardType, businessType) " +
            "VALUES(#{merchantID}, #{name}, #{password}, #{description}, #{cardDescription}, #{address}, #{merchantLogoURL}, #{cardLogoURL}, #{proportion}, #{activityTheme}, #{activityDescription}, #{cardType}, #{businessType})";
    final String loginMerchant = "SELECT * FROM merchant WHERE MerchantID = #{merchantID} AND password = #{password}";
    final String updateMerchantName = "UPDATE merchant SET name = #{name} WHERE merchantID = #{merchantID}";
    final String updateMercahntDescription = "UPDATE merchant SET description = #{description} WHERE merchantID = #{merchantID}";
    final String updateMerchantCardDescription = "UPDATE merchant SET cardDescription = #{cardDescription} WHERE merchantID = #{merchantID}";
    final String updateMercahntAddress = "UPDATE merchant SET address = #{address} WHERE merchantID = #{merchantID}";
    final String updateMerchantLogo = "UPDATE merchant SET merchantLogoURL = #{merchantLogoURL} WHERE MerchantID = #{merchantID}";
    final String updateMerchantCardLogo = "UPDATE merchant SET cardLogoURL = #{cardLogoURL} WHERE MerchantID = #{merchantID}";
    final String updateActivityTheme = "UPDATE merchant SET activityTheme = #{activityTheme} WHERE MerchantID = #{merchnatID}";
    final String updateActivityDescription = "\"UPDATE merchant SET activityDescription = #{activityDescription} WHERE MerchantID = #{merchnatID}";
    final String updateCardType = "UPDATE merchat SET cardType = #{cardType} WHERE MerchantID = #{merchantID}";
    final String updateBusinessType = "UPDATE merchat SET businessType = #{businessType} WHERE MerchantID = #{merchantID}";
    final String getSome = "SELECT * FROM merchant ORDER BY name LIMIT #{start}, #{length}";
    final String getById = "SELECT * FROM merchant WHERE MerchantID = #{Mercantid}";
    final String changePassword = "UPDATE merchant SET password = #{password} WHERE MerchantID = #{merchantID}";
    final String getMerchantAmount = "SELECT COUNT(*) from merchant";


    @Select(getAllMerchant)
    List<Merchant> getAllMerchant();

    @Select(getAllMerchantID)
    List<String> getAllMerchantID();

    @Insert(addMerchant)
    int addMerchant(Merchant merchantDAO);

    @Select(loginMerchant)
    Merchant loginMerchant(@Param("merchantID") String merchantID, @Param("password") String password);

    @Update(updateMerchantName)
    int updateMerchantName(@Param("merchantID") String merchantID, @Param("name") String name);

    @Update(updateMercahntDescription)
    int updateMercahntDescription(@Param("merchantID") String merchantID, @Param("description") String description);

    @Update(updateMerchantCardDescription)
    int updateMerchantCardDescription(@Param("merchantID") String merchantID, @Param("cardDescription") String cardDescription);

    @Update(updateMercahntAddress)
    int updateMercahntAddress(@Param("merchantID") String merchantID, @Param("address") String address);

    @Update(updateMerchantLogo)
    int updateMerchantLogo(@Param("merchantLogoURL") String merchantLogoURL, @Param("merchantID") String merchantID);

    @Update(updateMerchantCardLogo)
    int updateMerchantCardLogo(@Param("cardLogoURL") String cardLogoURL, @Param("merchantID") String merchantID);

    @Update(updateActivityTheme)
    int updateActivityTheme(@Param("merchantID") String merchantID, @Param("activityTheme") String activityTheme);

    @Update(updateActivityDescription)
    int updateActivityDescription(@Param("merchantID") String merchantID, @Param("activityDescription") String activityDescription);

    @Update(updateCardType)
    int updateCardType(Merchant merchant);

    @Update(updateBusinessType)
    int updateBusinessType(Merchant merchant);

    //The return sequence will be [start+1, start+2, ,,, start+length].
    @Select(getSome)
    List<Merchant> select(@Param("start") int start, @Param("length") int length);

    @Select(getById)
    Merchant selectByID(String MerchantID);

    @Update(changePassword)
    int changePassword(@Param("merchantID") String merchantID, @Param("password") String password);

    @Select(getMerchantAmount)
    int getMerchantAmount();

}
