package com.kingthy.conf;

import java.security.PrivateKey;
import java.util.ArrayList;

import com.kingthy.util.httpconnetion.HttpConnectionUtil;
import com.kingthy.model.ClassificationResult;
import com.kingthy.util.ConfigUtil;
import com.kingthy.util.ErrorUtil;
import com.kingthy.util.SignatureAndVerifyUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author soap API
 */
public class Api
{
    private String secretId;
    
    private String url;
    
    private PrivateKey privateKey;
    
    /**
     * @param secretId 用户secretId
     * @param pkPath 用户私钥
     */
    public Api(String secretId, String pkPath, String requestUrl)
    {
        if (null == requestUrl)
        {
            requestUrl = "http://api.open.tuputech.com/v3/recognition/";
        }
        this.secretId = secretId;
        this.url = requestUrl + secretId;
        this.privateKey = SignatureAndVerifyUtil.readPrivateKey(pkPath);
    }
    
    /**
     * 
     * @param fileType 传入的数据类型，ConfigUtil.UPLOAD_TYPE.UPLOAD_IMAGE_TYPE为本地文件 ConfigUtil.UPLOAD_TYPE.UPLOAD_URI_TYPE 为图片
     *            Url
     * @param fileLists 文件集合
     * @param tags [可选] 用于给图片附加额外信息（比如：直播客户可能传房间号，或者主播ID信息）。方便后续根据tag搜索到相关的图片
     * 
     * @return
     */
    public JSONObject doApiTest(String fileType, ArrayList<String> fileLists, String... tags)
    {
        if (fileLists == null || fileLists.isEmpty())
        {
            return ErrorUtil.getErrorMsg(ErrorUtil.ERROR_CODE_NO_FILE, "");
        }
        String timestamp = Math.round(System.currentTimeMillis() / 1000.0) + "";
        String nonce = Math.random() + "";
        String signString = secretId + "," + timestamp + "," + nonce;
        
        String signature = SignatureAndVerifyUtil.Signature(privateKey, signString);

        ClassificationResult classificationResult = null;
        
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        float time = 0;
        try
        {
            // 得到签名
            if (fileType == ConfigUtil.UPLOAD_TYPE.UPLOAD_IMAGE_TYPE)
            {
                classificationResult =
                    HttpConnectionUtil.uploadImage(url, secretId, timestamp, nonce, signature, fileLists, tags);
            }
            else if (fileType == ConfigUtil.UPLOAD_TYPE.UPLOAD_URI_TYPE)
            {
                classificationResult = HttpConnectionUtil.uploadUri(url, timestamp, nonce, signature, fileLists, tags);
            }
            if (classificationResult!=null && classificationResult.getResultCode() == 200)
            {
                String result = classificationResult.getResult();
                // 判断当前字符串的编码格式
                if (result.equals(new String(result.getBytes("iso8859-1"), "iso8859-1")))
                {
                    result = new String(result.getBytes("iso8859-1"), "utf-8");
                }
                JSONObject jsonObject = JSONObject.fromObject(result);
                
                String resultJson = jsonObject.getString("json");
                String code = JSONObject.fromObject(resultJson).getString("code");
                if (Integer.valueOf(code) == 0)
                {
                    String result_signature = jsonObject.getString("signature");
                    // 进行验证
                    boolean verify = SignatureAndVerifyUtil.Verify(result_signature, resultJson);
                    endTime = System.currentTimeMillis();
                    time = (float)(endTime - startTime) / (float)1000;
                    if (verify)
                    {
                        System.out.println("TUPU API: response verify succeed, total time" + time + "s");
                        return JSONObject.fromObject(resultJson);
                    }
                    else
                    {
                        System.out.println("TUPU API: response verify failed, total time" + time + "s");
                        return ErrorUtil.getErrorMsg(ErrorUtil.ERROR_CODE_RESULT_VERIFY_FAILED, "");
                    }
                }
                else
                {
                    System.out.println("TUPU API: response verify failed, total time" + time + "s");
                    return ErrorUtil.getErrorMsg(Integer.valueOf(code), "");
                }
            }
            else
            {
                System.out.println("TUPU API: response verify failed, total time" + time + "s");
                return ErrorUtil.getErrorMsg(classificationResult.getResultCode(), "");
            }
        }
        catch (Exception e)
        {
            System.out.println("TUPU API: response verify failed, total time" + time + "s");
            System.out.println("TUPU API: response verify failed, error is " + e.getMessage());
            return ErrorUtil.getErrorMsg(ErrorUtil.ERROR_CODE_OTHERS, e.getMessage());
        }
    }
}