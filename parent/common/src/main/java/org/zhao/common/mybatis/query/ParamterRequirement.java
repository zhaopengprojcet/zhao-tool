package org.zhao.common.mybatis.query;

import java.util.HashMap;
import java.util.Map;

/**
 * 关系查询实体
 * @author j_zhao
 *
 */
@Deprecated
public class ParamterRequirement {
	private Map<String, Map<String, String>> require = new HashMap<String, Map<String, String>>();

	public void setRequire(Map<String, Map<String, String>> require) {
		this.require = require;
	}

	public ParamterRequirement() {
		this.require.put("greaterThan", null);
		this.require.put("lessThan", null);
		this.require.put("greaterThanAndEquals", null);
		this.require.put("lessThanAndEquals", null);
		this.require.put("notEquality", null);
		this.require.put("similar", null);
		this.require.put("allColom", new HashMap<String, String>());
	}
	/**
	 * 添加列
	 * create by j_zhao
	 *
	 */
	private void addNewColom(String colom) {
		if (!((Map<String, String>) this.require.get("allColom")).containsKey(colom)) {
			Map<String, String> m = this.require.get("allColom");
			m.put(colom, colom);
			this.require.put("allColom", m);
		}
	}
	/**
	 * 获得结果所需集合
	 * create by j_zhao
	 *
	 */
	public Map<String, Map<String, String>> getParamter() {
		return this.require;
	}
	/**
	 * 大于
	 * create by j_zhao
	 *
	 */
	public void addGreaterThan(String colom) {
		if (this.require.get("greaterThan") == null) {
			this.require.put("greaterThan", new HashMap<String, String>());
		}
		Map<String, String> m = this.require.get("greaterThan");
		if (!m.containsKey(colom)) {
			m.put(colom, colom);
			this.require.put("greaterThan", m);
			addNewColom(colom);
		}
	}
	/**
	 * 小于
	 * create by j_zhao
	 *
	 */
	public void addLessThan(String colom) {
		if (this.require.get("lessThan") == null) {
			this.require.put("lessThan", new HashMap<String, String>());
		}
		Map<String, String> m = this.require.get("lessThan");
		if (!m.containsKey(colom)) {
			m.put(colom, colom);
			this.require.put("lessThan", m);
			addNewColom(colom);
		}
	}
	/**
	 * 大于等于
	 * create by j_zhao
	 *
	 */
	public void addGreaterThanAndEquals(String colom) {
		if (this.require.get("greaterThanAndEquals") == null) {
			this.require.put("greaterThanAndEquals", new HashMap<String, String>());
		}
		Map<String, String> m = this.require.get("greaterThanAndEquals");
		if (!m.containsKey(colom)) {
			m.put(colom, colom);
			this.require.put("greaterThanAndEquals", m);
			addNewColom(colom);
		}
	}
	/**
	 * 小于等于
	 * create by j_zhao
	 *
	 */
	public void addLessThanAndEquals(String colom) {
		if (this.require.get("lessThanAndEquals") == null) {
			this.require.put("lessThanAndEquals", new HashMap<String, String>());
		}
		Map<String, String> m = this.require.get("lessThanAndEquals");
		if (!m.containsKey(colom)) {
			m.put(colom, colom);
			this.require.put("lessThanAndEquals", m);
			addNewColom(colom);
		}
	}
	/**
	 * 不等于
	 * create by j_zhao
	 *
	 */
	public void addNotEquality(String colom) {
		if (this.require.get("notEquality") == null) {
			this.require.put("notEquality", new HashMap<String, String>());
		}
		Map<String, String> m = this.require.get("notEquality");
		if (!m.containsKey(colom)) {
			m.put(colom, colom);
			this.require.put("notEquality", m);
			addNewColom(colom);
		}
	}
	/**
	 * 等于
	 * create by j_zhao
	 *
	 */
	public void addSimilar(String colom) {
		if (this.require.get("similar") == null) {
			this.require.put("similar", new HashMap<String, String>());
		}
		Map<String, String> m = this.require.get("similar");
		if (!m.containsKey(colom)) {
			m.put(colom, colom);
			this.require.put("similar", m);
			addNewColom(colom);
		}
	}
}