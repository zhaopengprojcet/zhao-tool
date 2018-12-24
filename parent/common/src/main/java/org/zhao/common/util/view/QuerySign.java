package org.zhao.common.util.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.zhao.common.util.CookieUtil;

public class QuerySign {

	private static Logger logger = Logger.getLogger(QuerySign.class);
	
	public static ResultContent<JSONObject> deQuery(String jr ,HttpServletRequest request , String... param) {
		String key = CookieUtil.getCookieByName(request, CookieUtil.QUERY_COOKIE_NAME).getValue();
		JSONObject json = null;
		try {
			 json = JSONObject.fromObject(aesDecrypt(hexStringToString(jr), key));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("请求解析错误");
		}
		if(json == null) {
			 throw new RuntimeException("请求参数缺失");
		}
		logger.info("前端请求解密【"+json.toString()+"】");
		 if(!json.containsKey("_tms")) {
			 throw new RuntimeException("请求参数缺失");
		 }
		 try {
			long time = json.getLong("_tms");
			if(System.currentTimeMillis() - time > 10 * 60 * 1000) {
				throw new RuntimeException("请求超时");
			}
		} catch (Exception e) {
			throw new RuntimeException("请求格式错误");
		}
		 if(param != null && param.length > 0) {
			 for (String string : param) {
				if(!json.containsKey(string)) {
					throw new RuntimeException("参数缺失");
				}
			}
		 }
		 return new ResultContent<JSONObject>(ResultContent.SUCCESS, "解密成功",json);
	}
	
	
	public static <T> ResultContent<T> deQueryToBean(String jr ,HttpServletRequest request , Class<T> c ,boolean check ,String... param) {
		ResultContent<JSONObject> result = deQuery(jr,request,param);
		if(check) {
			Field[] fields = c.getDeclaredFields();
			T t = (T)JSONObject.toBean(result.getData(), c);
			//判断当前操作是新增还是修改
			int updateType = 0;
			for (Field field : fields) {
				if(field.isAnnotationPresent(UpdateView.class)) {
					UpdateView uv = field.getAnnotation(UpdateView.class);
					if(uv.key()) {
						Object value = null;
						try {
							String firstLetter = field.getName().substring(0, 1).toUpperCase();    
		    	            String getter = "get" + firstLetter + field.getName().substring(1);    
		    	            Method method = c.getMethod(getter, new Class[] {});    
		    	            value = method.invoke(t, new Object[] {});
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException("请求效验失败");
						}
						if(value != null) {
							updateType = 2;
						}
						else {
							updateType = 1;
						}
						break;
					}
				}
			}
			
			
			for (Field field : fields) {
				if(field.isAnnotationPresent(UpdateView.class)) {
					UpdateView uv = field.getAnnotation(UpdateView.class);
					if(updateType == 1) {
						//新增
						if(uv.updateType() == UpdateTypeEm.UPDATE) continue;
					}
					else if(updateType == 2) {
						//修改
						if(uv.updateType() == UpdateTypeEm.ADD) continue;
					}
					if(!StringUtils.isEmpty(uv.check())) {
						Object value = null;
						try {
							String firstLetter = field.getName().substring(0, 1).toUpperCase();    
		    	            String getter = "get" + firstLetter + field.getName().substring(1);    
		    	            Method method = c.getMethod(getter, new Class[] {});    
		    	            value = method.invoke(t, new Object[] {});
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException("请求效验失败");
						}
						JSONObject checkPar = JSONObject.fromObject(uv.check());
						if(checkPar.containsKey("required")) {
							logger.error("请求字段不能为空【"+c.getCanonicalName()+"-->"+field.getName()+"】");
							if(value == null) throw new RuntimeException("请求效验未通过");
						}
						if(checkPar.containsKey("maxLength")) {
							logger.error("请求字段超长【"+c.getCanonicalName()+"-->"+field.getName()+"】");
							if(value != null && value.toString().length() > checkPar.getInt("maxLength")) throw new RuntimeException("请求效验未通过");
						}
						if(checkPar.containsKey("minLength")) {
							logger.error("请求字段长度不足【"+c.getCanonicalName()+"-->"+field.getName()+"】");
							if(value != null && value.toString().length() < checkPar.getInt("minLength")) throw new RuntimeException("请求效验未通过");
						}
						if(field.getType() == int.class || field.getType() == Integer.class 
								||field.getType() == double.class || field.getType() == Double.class
								||field.getType() == float.class || field.getType() == Float.class) {
							if(checkPar.containsKey("maxNumber")) {
								logger.error("请求字段过大【"+c.getCanonicalName()+"-->"+field.getName()+"】");
								if(new Double(value != null?value.toString():"0") > checkPar.getInt("maxNumber")) throw new RuntimeException("请求效验未通过");
							}
							if(checkPar.containsKey("minNumber")) {
								logger.error("请求字段过小【"+c.getCanonicalName()+"-->"+field.getName()+"】");
								if(new Double(value != null?value.toString():"0") < checkPar.getInt("minNumber")) throw new RuntimeException("请求效验未通过");
							}
						}
					}
				}
			}
		}
		return new ResultContent<T>(ResultContent.SUCCESS , "解密成功",(T)JSONObject.toBean(result.getData(), c));
	}
	private static String hexStringToString(String s) {
	    if (s == null || s.equals("")) {
	        return null;
	    }
	    s = s.replace(" ", "");
	    byte[] baKeyword = new byte[s.length() / 2];
	    for (int i = 0; i < baKeyword.length; i++) {
	        try {
	            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    try {
	        s = new String(baKeyword, "UTF-8");
	        new String();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	    return s;
	}
	
	private static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {  
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);  
    }  
    
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
  
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  
        return new String(decryptBytes);  
    }
    
    private static byte[] base64Decode(String base64Code) throws Exception{  
        return StringUtils.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);  
    }
}
