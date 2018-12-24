package org.zhao.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

public class RandomUtils {

	// 验证码图片的宽度。        
    private static int width = 60;        
      
   // 验证码图片的高度。        
    private static int height = 20;        
      
   // 验证码字符个数        
    private static int codeCount = 4;        
      
    private static int x = 0;        
      
   // 字体高度        
    private static int fontHeight;        
      
    private static int codeY;     
    
    private static final String RANDOM_CODE = "VALIDATE_RANDOM_CODE";//session中缓存code值的名称
    
    private static final String RANDOM_PIC_WIDHT_HEIGHT= "pwh";
    
    private static char[] codeSequence = { 
    	'a','b','c','d','e','f','g','h','j','k','m','n','p','q','r','s','t','u','v','w','x','y','z',
    	'A','B','C','D','E','F','G','H','I','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z',
    	'1', '2', '3', '4', '5', '6', '7', '8', '9' };        
   
    /**
     * 验证码效验
     * @param code
     * @param request
     * @return
     */
   public static boolean validateCode(String code , HttpServletRequest request) {
	   if(request.getSession().getAttribute(RANDOM_CODE) == null) return false;
	   if(StringUtils.isEmpty(code)) return false;
	   if(code.equalsIgnoreCase(request.getSession().getAttribute(RANDOM_CODE).toString())) return true;
	   return false;
   }
   
   /**
    * 生成验证图片
    * @param request
    * @param reponse
    */
   public static void drawPic(HttpServletRequest request ,HttpServletResponse reponse) {
	   if(request.getParameter(RANDOM_PIC_WIDHT_HEIGHT) != null) {
		   try {
			   height = Integer.parseInt(request.getParameter(RANDOM_PIC_WIDHT_HEIGHT).toString().split("X")[0]);
			   width = Integer.parseInt(request.getParameter(RANDOM_PIC_WIDHT_HEIGHT).toString().split("X")[1]);
		   } catch (Exception e) {
		   }
	   }
	   x = width / (codeCount + 1);        
	   fontHeight = height - 2;        
	   codeY = height - 4;    
	   BufferedImage buffImg = new BufferedImage(width, height,        
	           BufferedImage.TYPE_INT_RGB);        
	   Graphics2D g = buffImg.createGraphics();        
	  
	   // 创建一个随机数生成器类        
	   Random random = new Random();        
	  
	   // 将图像填充为白色        
	   g.setColor(Color.WHITE);        
	   g.fillRect(0, 0, width, height);        
	  
	   // 创建字体，字体的大小应该根据图片的高度来定。        
	   Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);        
	   // 设置字体。        
	   g.setFont(font);        
	  
	   // 画边框。        
	//   g.setColor(Color.BLACK);        
	//   g.drawRect(0, 0, width - 1, height - 1);        
	  
	   // 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。        
	   g.setColor(Color.BLACK);        
	   for (int i = 0; i < 10; i++) {        
	       int x2 = random.nextInt(width);        
	       int y2 = random.nextInt(height);        
	       int xl = random.nextInt(width);        
	       int yl = random.nextInt(height);        
	       g.drawLine(x2, y2, x2 + xl, y2 + yl);      
	   }        
	  
	   // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。        
	   StringBuffer randomCode = new StringBuffer();  
	     
	   int red = 0, green = 0, blue = 0;        
	  
	   // 随机产生codeCount数字的验证码。        
	   for (int i = 0; i < codeCount; i++) {        
	       // 得到随机产生的验证码数字。        
	       String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);        
	       // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。        
	       red = random.nextInt(255);        
	       green = random.nextInt(255);        
	       blue = random.nextInt(255);        
	  
	       // 用随机产生的颜色将验证码绘制到图像中。        
	       g.setColor(new Color(red, green, blue));        
	       g.drawString(strRand, (i + 1) * x, codeY);        
	    
	       randomCode.append(strRand);        
	   }        
	   // 将四位数字的验证码保存到Session中。        
	   request.getSession().setAttribute(RANDOM_CODE, randomCode.toString());        
	   ServletOutputStream sos;  
	   try {  
	       sos = reponse.getOutputStream();  
	       ImageIO.write(buffImg, "jpeg", sos);        
	       sos.close();        
	   } catch (IOException e) {   
	       e.printStackTrace();  
	   }        
   }
   
   
   public static final String[] num_str = { "2", "3", "4", "5", "6", 
	    "7", "8", "9" };
	  public static final String[] letter_str = { "a", "b", "c", "d", "e", "f", 
	    "g", "h", "j", "k", "m", "n", "p", "q", "r", "s", 
	    "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", 
	    "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", 
	    "T", "U", "V", "W", "X", "Y", "Z" };
	  public static final String[] all_str = { "2", "3", "4", "5", "6", 
	    "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "j", 
	    "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", 
	    "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
	    "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", 
	    "X", "Y", "Z" };
	  public static final int WIDTH = 130;
	  public static final int HEIGHT = 48;
	  public static final String RANDOM_STR_SESSION = "random_str_session";

	  public static String getRandomStrByNum(int length)
	  {
	    if (length < 1)
	      return "";
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 8.0D);
	      str.append(num_str[index]);
	    }
	    return str.toString();
	  }

	  public static String getRandomStrByNum(int length, String separator, int space)
	  {
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 8.0D);
	      str.append(num_str[index]);
	      if ((i < length - 1) && ((i + 1) % space == 0))
	        str.append(separator);
	    }

	    return str.toString();
	  }

	  public static String getRandomStrByLetter(int length) {
	    if (length < 1)
	      return "";
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 47.0D);
	      str.append(letter_str[index]);
	    }
	    return str.toString();
	  }

	  public static String getRandomStrByLetter(int length, String separator, int space)
	  {
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 47.0D);
	      str.append(letter_str[index]);
	      if ((i < length - 1) && ((i + 1) % space == 0))
	        str.append(separator);
	    }

	    return str.toString();
	  }

	  public static String getRandomStrByAll(int length) {
	    if (length < 1)
	      return "";
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 55.0D);
	      str.append(all_str[index]);
	    }
	    return str.toString();
	  }

	  public static String getRandomStrByAll(int length, String separator, int space)
	  {
	    StringBuffer str = new StringBuffer();
	    for (int i = 0; i < length; ++i) {
	      int index = (int)(Math.random() * 55.0D);
	      str.append(all_str[index]);
	      if ((i < length - 1) && ((i + 1) % space == 0))
	        str.append(separator);
	    }

	    return str.toString();
	  }

	  public static String getPrimaryKey() {
	    return System.currentTimeMillis() + getRandomStrByAll(20, "-", 5);
	  }

	
	  public List<String> r(int length) {
		  List<String> c = new ArrayList<String>();
		  for(char t :codeSequence) {
			  c.add(t+"");
		  }
		  List<String> strs = new ArrayList<String>();
		  for(int i = 0 ; i < length-1 ; i++) {
			  strs = addStr(strs ,c);
			  int v = 0;
		  }
		  return strs;
	  }
	  
	  public List<String> addStr(List<String> strs , List<String> s) {
		  if(strs.size() < 1) {
			  strs = s;
		  }
		  List<String> newS = new ArrayList<String>();
		  for(int i = 0 ; i < strs.size() ; i++) {
			  String b = strs.get(i);
			  for (String string : s) {
				if(b.indexOf(string) == -1) {
					newS.add(b+string);
				}
			  }
		  }
		  
		return newS;
	  }
	  
	  public static void main(String[] args) {
		for(int i = 0 ; i < 10 ; i ++) {
			System.out.println(RandomUtils.getPrimaryKey());
		}
	}
}
 