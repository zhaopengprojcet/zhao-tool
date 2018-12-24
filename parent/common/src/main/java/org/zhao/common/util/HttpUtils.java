package org.zhao.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.zhao.common.util.view.ResultContent;

public class HttpUtils {

	private static String UTF_8 = "UTF-8";
	
	public static ResultContent<String> get(String url) {
		String content = "";

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					content = EntityUtils.toString(entity, UTF_8);
				}
			} finally {
				response.close();
			}
			return (ResultContent<String>) JSONObject.toBean(JSONObject.fromObject(content), ResultContent.class);
		} catch (ClientProtocolException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (ParseException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (IOException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (Exception e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
			}
		}
	}

	public static ResultContent<String> post(String url, Map<String, String> values) {
		String content = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : values.entrySet()) {
			formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

		}

		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, UTF_8);
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					content = EntityUtils.toString(entity, UTF_8);
				}
			} finally {
				response.close();
			}
			return (ResultContent<String>) JSONObject.toBean(JSONObject.fromObject(content), ResultContent.class);
		} catch (ClientProtocolException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (UnsupportedEncodingException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (IOException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (Exception e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
			}
		}

	}

	public static ResultContent<String> postJson(String url, String jsonString) {
		String result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			httppost.addHeader("Content-type","application/json; charset=utf-8");
			httppost.setHeader("Accept", "application/json");
			httppost.setEntity(new StringEntity(jsonString, Charset.forName(UTF_8)));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if(entity!=null) {
				result = EntityUtils.toString(entity, UTF_8);
			}
			return (ResultContent<String>) JSONObject.toBean(JSONObject.fromObject(result), ResultContent.class);
		} catch(Exception e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} finally {
			try {
				if(response!=null) {
					response.close();
				}
			} catch(Exception e) {
				return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
			}
			try {
				httpclient.close();
			} catch(Exception e) {
				return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
			}
		}
	}
	
	public static ResultContent<String> postJson(String url, String jsonString, Map<String, String> headers) {
		String result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			httppost.addHeader("Content-type","application/json; charset=utf-8");
			httppost.setHeader("Accept", "application/json");
			if(headers!=null){
				for(String header : headers.keySet()) {
					httppost.setHeader(header, headers.get(header));
	            }
			}
			
			httppost.setEntity(new StringEntity(jsonString, Charset.forName(UTF_8)));
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if(entity!=null) {
				result = EntityUtils.toString(entity, UTF_8);
			}
			return (ResultContent<String>) JSONObject.toBean(JSONObject.fromObject(result), ResultContent.class);
		} catch(Exception e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} finally {
			try {
				if(response!=null) {
					response.close();
				}
			} catch(Exception e) {
				return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
			}
			try {
				httpclient.close();
			} catch(Exception e) {
				return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
			}
		}
	}
	@SuppressWarnings("unchecked")
	public static ResultContent<String> postObject(String url, Map<String, Object> values) {
		String content = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			Object value = entry.getValue();
			if (value == null) {
				continue;
			}
			if (Iterable.class.isAssignableFrom(value.getClass())) {
				for (Object item : (Iterable<Object>) value) {
					formparams.add(new BasicNameValuePair(entry.getKey(), item.toString()));
				}
			} else if (value.getClass().isArray()) {
				for (Object item : (Object[]) value) {
					formparams.add(new BasicNameValuePair(entry.getKey(), item.toString()));
				}
			} else {
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
		}

		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, UTF_8);
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					content = EntityUtils.toString(entity, UTF_8);
				}
				return (ResultContent<String>) JSONObject.toBean(JSONObject.fromObject(content), ResultContent.class);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (UnsupportedEncodingException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (IOException e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} catch (Exception e) {
			return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
		} finally {
			try {
				httpclient.close();
				httppost.releaseConnection();
			} catch (IOException e) {
				return new ResultContent<String>(ResultContent.ERROR, e.getLocalizedMessage());
			}
		}

	}

	
}
