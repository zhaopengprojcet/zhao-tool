package org.zhao.common.util.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.zhao.common.pojo.model.ZmenuButtonModel;
import org.zhao.common.pojo.model.ZmenuFieldModel;
import org.zhao.common.pojo.model.ZmenuModel;
import org.zhao.common.pojo.model.ZmenuSearchModel;
import org.zhao.common.util.SessionUtil;

public class TablelListUtils {

	private static final String TREE_TABLE_TYPE = "tree";
	private static final String LIST_TABLE_TYPE = "table";
	
	/**构建页面中的按钮，查询等权限过滤及展示列表
	 * 
	 * @param menuId
	 * @param request
	 * @param model
	 * @param httpMenuButtonSV
	 */
	public static String addSessionButtons(String menuId , HttpServletRequest request , Model model) {
		if(!StringUtils.isEmpty(menuId)) {
			ResultContent<JSONObject> json = QuerySign.deQuery(menuId, request);
			if(json.getCode().equals(ResultContent.SUCCESS)) {
				menuId = json.getData().getString("menuId");
				List<ZmenuButtonModel> addBtns = new ArrayList<ZmenuButtonModel>();
				List<ZmenuFieldModel> addFields = new ArrayList<ZmenuFieldModel>();
				List<ZmenuSearchModel> searchs = new ArrayList<ZmenuSearchModel>();
				List linkedTreeMap = (List) request.getSession().getAttribute(SessionUtil.ROLE_POWERS);
				JSONArray array = JSONArray.fromObject(linkedTreeMap);
				Map<String, Class> classMap = new HashMap<String, Class>();
				classMap.put("buttons", ZmenuButtonModel.class);
				classMap.put("fields", ZmenuFieldModel.class);
				classMap.put("searchs", ZmenuSearchModel.class);
				
				if(!CollectionUtils.isEmpty(linkedTreeMap)){
					List<ZmenuModel> list = JSONArray.toList(array, ZmenuModel.class, classMap);
					ZmenuModel zmenu = null;
					for (ZmenuModel zmenuModel : list) {
						if(menuId.equals(zmenuModel.getId())) {
							addBtns = zmenuModel.getButtons();
							addFields = zmenuModel.getFields();
							searchs = zmenuModel.getSearchs();
							//加载菜单对应页面信息
							zmenu = zmenuModel;
							break;
						}
					}
					if(model != null) {
						model.addAttribute("buttons", addBtns);
						model.addAttribute("fields", addFields);
						model.addAttribute("searchs", searchs);
					}
					if(zmenu.getListType().equals(TREE_TABLE_TYPE)) {
						String parame = zmenu.getListInner();
						parame += "striped:true,nowrap:true,loadMsg:'数据加载中...',rownumbers:true,singleSelect:true,toolbar:'#grid-table-toolbar',loadFilter:loadErrorDataAlert";
						model.addAttribute("menu_parame", parame);
						return "base/tree_index";
					}
					else if(zmenu.getListType().equals(LIST_TABLE_TYPE)) {
						String parame = zmenu.getListInner();
						parame += "striped:true,nowrap:true,loadMsg:'数据加载中...',rownumbers:true,pagination:true,singleSelect:true,toolbar:'#grid-table-toolbar',loadFilter:loadErrorDataAlert";
						model.addAttribute("menu_parame", parame);
						return "base/table_index";
					}
				}
			}
		}
		return null;
	}
	
	public static <T> String buildUpdate(Class c ,String updateUrl, Model model , T... data) {
		if(c == null) {
			throw new RuntimeException("构建更新页面失败");
		}
		List<UpdateViewModel> list = new ArrayList<UpdateViewModel>();
		Field[] fields = c.getDeclaredFields();
		int loadCount = 0;
		for (Field field : fields) {
			if(field.isAnnotationPresent(UpdateView.class)) {
				UpdateView uv = field.getAnnotation(UpdateView.class);
				if(uv.updateType() == UpdateTypeEm.ADD_AND_UPDATE || (uv.updateType() == UpdateTypeEm.ADD &&  data.length < 1) || (uv.updateType() == UpdateTypeEm.UPDATE &&  data.length > 0)){
					UpdateViewModel uvm = new UpdateViewModel();
					uvm.setCheck(uv.check());
					uvm.setName(uv.name());
					uvm.setText(uv.text());
					uvm.setType(uv.type().getValue());
					uvm.setLoadValue(uv.loadValue());
					uvm.setLoadValueUrl(uv.loadValueUrl());
					uvm.setTextField(uv.textField());
					uvm.setValueField(uv.valueField());
					list.add(uvm);
					if(!StringUtils.isEmpty(uvm.getLoadValueUrl()) && (UpdateTypeEm.SELECT.getValue().equals(uvm.getType())  || UpdateTypeEm.SELECT_TREE.getValue().equals(uvm.getType()))) loadCount++;
				}
			}
		}
		model.addAttribute("update_view", list);
		model.addAttribute("update_url", updateUrl);
		model.addAttribute("loadNum", loadCount);
		if(data != null && data.length > 0) model.addAttribute("data", data[0]);
		return "base/update";
	}
}
