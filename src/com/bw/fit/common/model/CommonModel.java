package com.bw.fit.common.model;
import static com.bw.fit.common.util.PubFun.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommonModel extends BaseModel implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String staff_number;
	private String staff_name;
	private String password;
	private String passwd;
	private String phone;
	private String state ;
	private String address;
	private String temp_str1;
	private String temp_str2;
	private String temp_str3;
	private String temp_str4;
	private int temp_int1;
	private int temp_int2;
	private int temp_int3;
	private int temp_int4;

	private String postion_name;
	private String desp;
	private String role_name;
	private String parent_id ;
	private String foreign_id;
	private String menu_name;
	private String menu_path;
	private String menu_url; 
	private String menu_level ;
	private String iscontrol ;
	private String default_action ;
	private String params ;
	private String company_id ;
	private String company_name;
	private String company_address;
	private String company_type_id ;
	private String company_type_name;
	private String parent_company_name;
	private String parent_company_id;
	private int company_order;
	
	private List temp_list ;
	private List temp_list2 ;
	private List temp_list3 ;
	private String dict_name;
	private String dict_value;
	private String dict_remark;
	private int num;
	private String can_add ;
	private String can_edit ;
	private String can_del ;
	
	private String operate_code ;
	private String operate_name ;
	private String operate_type ;
	private String operate_css ;
	private String remark ;
	private String operate_target; 
	private String group_name;
	private String job_name;
	private String elementId;
	private String elementName;
	
	private String flowDefinitionId;
	private String node_code ;
	private String dealers;
	private String dealernames;
	
	
	
	
	public String getFlowDefinitionId() {
		return flowDefinitionId;
	}
	public void setFlowDefinitionId(String flowDefinitionId) {
		this.flowDefinitionId = flowDefinitionId;
	}
	public String getNode_code() {
		return node_code;
	}
	public void setNode_code(String node_code) {
		this.node_code = node_code;
	}
	public String getDealers() {
		return dealers;
	}
	public void setDealers(String dealers) {
		this.dealers = dealers;
	}
	public String getDealernames() {
		return dealernames;
	}
	public void setDealernames(String dealernames) {
		this.dealernames = dealernames;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public List getTemp_list2() {
		return temp_list2;
	}
	public void setTemp_list2(List temp_list2) {
		this.temp_list2 = temp_list2;
	}
	public List getTemp_list3() {
		return temp_list3;
	}
	public void setTemp_list3(List temp_list3) {
		this.temp_list3 = temp_list3;
	}
	public int getTemp_int1() {
		return temp_int1;
	}
	public void setTemp_int1(int temp_int1) {
		this.temp_int1 = temp_int1;
	}
	public int getTemp_int2() {
		return temp_int2;
	}
	public void setTemp_int2(int temp_int2) {
		this.temp_int2 = temp_int2;
	}
	public int getTemp_int3() {
		return temp_int3;
	}
	public void setTemp_int3(int temp_int3) {
		this.temp_int3 = temp_int3;
	}
	public int getTemp_int4() {
		return temp_int4;
	}
	public void setTemp_int4(int temp_int4) {
		this.temp_int4 = temp_int4;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public String getForeign_id() {
		return foreign_id;
	}
	public void setForeign_id(String foreign_id) {
		this.foreign_id = foreign_id;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getParent_company_id() {
		return parent_company_id;
	}
	public void setParent_company_id(String parent_company_id) {
		this.parent_company_id = parent_company_id;
	}
	public int getCompany_order() {
		return company_order;
	}
	public void setCompany_order(int company_order) {
		this.company_order = company_order;
	}
	public String getOperate_css() {
		return operate_css;
	}
	public void setOperate_css(String operate_css) {
		this.operate_css = operate_css;
	}
	public String getOperate_target() {
		return operate_target;
	}
	public void setOperate_target(String operate_target) {
		this.operate_target = operate_target;
	}
	public String getOperate_code() {
		return operate_code;
	}
	public void setOperate_code(String operate_code) {
		this.operate_code = operate_code;
	}
	public String getOperate_name() {
		return operate_name;
	}
	public void setOperate_name(String operate_name) {
		this.operate_name = operate_name;
	}
	public String getOperate_type() {
		return operate_type;
	}
	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	} 
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	} 
	public String getDict_name() {
		return dict_name;
	}
	public void setDict_name(String dict_name) {
		this.dict_name = dict_name;
	}
	public String getDict_value() {
		return dict_value;
	}
	public void setDict_value(String dict_value) {
		this.dict_value = dict_value;
	}
	public String getDict_remark() {
		return dict_remark;
	}
	public void setDict_remark(String dict_remark) {
		this.dict_remark = dict_remark;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getCan_add() {
		return can_add;
	}
	public void setCan_add(String can_add) {
		this.can_add = can_add;
	}
	public String getCan_edit() {
		return can_edit;
	}
	public void setCan_edit(String can_edit) {
		this.can_edit = can_edit;
	}
	public String getCan_del() {
		return can_del;
	}
	public void setCan_del(String can_del) {
		this.can_del = can_del;
	}
	public List getTemp_list() {
		return temp_list;
	}
	public void setTemp_list(List temp_list) {
		this.temp_list = temp_list;
	}
	public String getCompany_address() {
		return company_address;
	}
	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_type_id() {
		return company_type_id;
	}
	public void setCompany_type_id(String company_type_id) {
		this.company_type_id = company_type_id;
	}
	public String getCompany_type_name() {
		return company_type_name;
	}
	public void setCompany_type_name(String company_type_name) {
		this.company_type_name = company_type_name;
	}
	public String getParent_company_name() {
		return parent_company_name;
	}
	public void setParent_company_name(String parent_company_name) {
		this.parent_company_name = parent_company_name;
	}
	public String getTemp_str1() {
		return temp_str1;
	}
	public void setTemp_str1(String temp_str1) {
		this.temp_str1 = temp_str1;
	}
	public String getTemp_str2() {
		return temp_str2;
	}
	public void setTemp_str2(String temp_str2) {
		this.temp_str2 = temp_str2;
	}
	public String getTemp_str3() {
		return temp_str3;
	}
	public void setTemp_str3(String temp_str3) {
		this.temp_str3 = temp_str3;
	}
	public String getTemp_str4() {
		return temp_str4;
	}
	public void setTemp_str4(String temp_str4) {
		this.temp_str4 = temp_str4;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getDefault_action() {
		return default_action;
	}
	public void setDefault_action(String default_action) {
		this.default_action = default_action;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public String getMenu_path() {
		if(null==menu_path||"".equals(menu_path))
			return "-9";
		return menu_path;
	}
	public void setMenu_path(String menu_path) {
		this.menu_path = menu_path;
	}
	public String getMenu_url() {
		return menu_url;
	}
	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}
	public String getMenu_level() {
		return menu_level;
	}
	public void setMenu_level(String menu_level) {
		this.menu_level = menu_level;
	}
	public String getIscontrol() {
		return iscontrol;
	}
	public void setIscontrol(String iscontrol) {
		this.iscontrol = iscontrol;
	}
	public String getPostion_name() {
		return postion_name;
	}
	public void setPostion_name(String postion_name) {
		this.postion_name = postion_name;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getStaff_number() {
		return staff_number;
	}
	public void setStaff_number(String staff_number) {
		this.staff_number = staff_number;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	
}
