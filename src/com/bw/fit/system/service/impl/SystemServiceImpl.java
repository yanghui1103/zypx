package com.bw.fit.system.service.impl;

import static com.bw.fit.common.util.PubFun.getSysDate;
import static com.bw.fit.common.util.PubFun.getUUID;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.BaseModel;
import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.model.RbackException;
import com.bw.fit.common.service.impl.CommonServiceImpl;
import com.bw.fit.common.util.MD5;
import com.bw.fit.common.util.PropertiesUtil;
import com.bw.fit.system.lambda.SystemLambda;
import com.bw.fit.system.model.Postion;
import com.bw.fit.system.model.Role;
import com.bw.fit.system.model.Staff;
import com.bw.fit.system.persistence.BaseConditionVO;
import com.bw.fit.system.service.SystemService;

@Service
public class SystemServiceImpl implements SystemService {
	@Autowired
	private CommonDao commonDao;

	public String test() throws Exception {
		CommonServiceImpl c = new CommonServiceImpl();
		int g = (int) c.function(11, SystemLambda.f); //
		return String.valueOf(g);
	}

	@Override
	public JSONObject getOnLineSituation(HttpSession session, LogUser user,
			ServletContext servletContext) {
		List<LogUser> showList = (ArrayList) (servletContext
				.getAttribute("onLineUserList"));
		JSONObject json = new JSONObject();
		if (showList == null || showList.size() < 1) {
			json.put("res", "2");
			json.put("msg", "此帐号此IP可以登录使用");
			return json;
		}
		int LogUserMaxCnt = Integer.valueOf(PropertiesUtil
				.getValueByKey("user.login.maxcnt"));
		if (showList != null && (showList.size() >= LogUserMaxCnt)) {
			json.put("res", "1");
			json.put("msg", "在线人数已经超出上限数目:" + LogUserMaxCnt);
			return json;
		}
		List<LogUser> afterList = showList
				.parallelStream()
				.filter((n) -> n.getUser_cd().equalsIgnoreCase(
						user.getUser_cd())
						&& n.getIp().equalsIgnoreCase(user.getIp()))
				.collect(Collectors.toList());
		if (afterList != null || afterList.size() > 0) {
			json.put("res", "1");
			json.put("msg", "此帐号已经在别的地方登录");
			return json;
		}
		CommonModel c = new CommonModel();
		commonDao.getListData("systemSql.getStaffInfoByNumber", c);
		json.put("res", "2");
		json.put("msg", "此帐号此IP可以登录使用");
		return json;
	}

	@Override
	public Staff getStaffInfoByNumber(CommonModel c) {
		List<Staff> list = (ArrayList<Staff>) commonDao.getListData(
				"systemSql.getStaffInfoByNumber", c);
		if (list.size() != 1)
			return null;
		return list.get(0);
	}

	@Override
	public List<Role> getRoleListByStaffId(CommonModel c) {
		List<Role> list = (ArrayList<Role>) commonDao.getListData(
				"systemSql.getRoleListByStaffId", c);

		return list;
	}

	@Override
	public List<Postion> getPostionListByStaffId(CommonModel c) {
		List<Postion> list = (ArrayList<Postion>) commonDao.getListData(
				"systemSql.getPostionListByStaffId", c);

		return list;
	}

	@Override
	public JSONObject getPwdCheckResult(LogUser user) {
		JSONObject json = new JSONObject();
		String passwdMM = getPasswdMMOfStaff(user.getUser_cd(),
				user.getPasswd(),
				PropertiesUtil.getValueByKey("user.pw.slogmm"));
		CommonModel c = new CommonModel();
		c.setStaff_number(user.getUser_cd());
		Staff staff = getStaffInfoByNumber(c);
		if (staff == null) { // 用户不存在，或资料重复
			json.put("res", "1");
			json.put("msg", "用户不存在,或资料出错");
			return json;
		}
		MD5 m = new MD5();
		if (!passwdMM.equals(staff.getPassword())) {
			json.put("res", "1");
			json.put("msg", "密码有误");
			return json;
		}
		json.put("res", "2");
		json.put("msg", "密码正确");
		return json;
	}

	/***
	 * 加密
	 */
	@Override
	public String mmUserPassword(String staff_number, String passwd) {
		StringBuffer smm = new StringBuffer();
		MD5 m = new MD5();
		smm.append(PropertiesUtil.getValueByKey("user.pw.slogmm"));
		smm.append(staff_number);
		smm.append(passwd);
		return m.getMD5ofStr(smm.toString());
	}

	@Override
	public JSONObject getMenuTreeJsonByStaffId(CommonModel c) {
		JSONObject json = new JSONObject();
		List<CommonModel> list = new ArrayList<CommonModel>();
		List<CommonModel> list1 = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getMenuOfStaff", c);
		List<CommonModel> list2 = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getMenuOfStaffRole", c);
		List<CommonModel> list3 = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getMenuOfStaffGrp", c);
		list.addAll(list1);
		list.addAll(list2);
		list.addAll(list3);
		if (list == null || list.size() < 1) {
			json.put("res", "1");
			json.put("msg", "此用户暂无菜单权限");
			return json;
		}
		List<CommonModel> nodeList = list.stream().distinct()
				.sorted((n1, n2) -> +n1.getFdid().compareTo(n2.getFdid()))
				.collect(Collectors.toList()); // 去重

		// start
		List<CommonModel> levelList = nodeList.stream()
				.filter((n) -> "1".equals(n.getMenu_level()))
				.collect(Collectors.toList());
		json.put("list", getJSON(nodeList, levelList));
		// 拼接End

		json.put("res", "2");
		json.put("msg", "此用户拥有菜单权限");
		return json;
	}

	private boolean getExisteNode(List<CommonModel> list, CommonModel c) {
		List ls = list.stream()
				.filter((n) -> (n.getParent_id()).equals(c.getFdid()))
				.collect(Collectors.toList());
		if (ls.size() < 1)
			return false;
		return true;
	}

	private JSONArray getJSON(List<CommonModel> list2, List<CommonModel> list) {
		JSONArray array1 = new JSONArray();
		for (CommonModel cc : list) {
			JSONObject json2 = new JSONObject();
			json2.put("id", cc.getFdid());
			json2.put("level", cc.getMenu_level());
			json2.put("page_name", cc.getMenu_name());
			json2.put("page_path", cc.getMenu_path());
			json2.put("p_id", cc.getParent_id());
			json2.put("rel", "page" + cc.getFdid());
			json2.put("page_url", cc.getMenu_url());
			String fdid = cc.getFdid();
			List<CommonModel> childs = list2.stream()
					.filter((n) -> (n.getParent_id()).equals(fdid))
					.collect(Collectors.toList());
			if (childs.size() > 0) {
				json2.put("childs", getChildJSON(childs, fdid, list2));
			}

			array1.add(json2);
		}
		return array1;
	}

	private JSONArray getChildJSON(List<CommonModel> list, String fdid,
			List<CommonModel> alllist) {
		JSONArray array = new JSONArray();
		List<CommonModel> list2 = alllist.stream()
				.filter((n) -> fdid.equals(n.getParent_id()))
				.collect(Collectors.toList());

		for (CommonModel cc : list2) {
			JSONObject json2 = new JSONObject();
			json2.put("id", cc.getFdid());
			json2.put("level", cc.getMenu_level());
			json2.put("page_name", cc.getMenu_name());
			json2.put("page_path", cc.getMenu_path());
			json2.put("p_id", cc.getParent_id());
			json2.put("rel", "page" + cc.getFdid());
			json2.put("page_url", cc.getMenu_url());
			JSONArray arra2 = getChildJSON(list, cc.getFdid(), alllist);
			if (arra2.size() > 0) {
				json2.put("childs", arra2);
			}
			if (!getExisteNode(list, cc)) {
				array.add(json2);
			}
		}
		return array;

	}

	@Override
	public JSONObject updatePwd(CommonModel c) throws RbackException {
		// 修改密码
		JSONObject j = new JSONObject();
		j.put("res", "2");
		j.put("msg", "密码修改成功");
		Staff st = getStaffInfoByNumber(c);
		if (!mmUserPassword(c.getStaff_number(), c.getPasswd()).equals(
				st.getPassword())) {
			j = new JSONObject();
			j.put("res", "1");
			j.put("msg", "原密码错误,请重新输入");
			return j;
		}
		c.setTemp_str3(mmUserPassword(c.getStaff_number(), c.getTemp_str1()));
		commonDao.update("systemSql.updatePwd", c);
		return j;
	}

	@Override
	public List<CommonModel> getChildCompByCurrentComp(String fdid) {
		List<CommonModel> list = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getChildCompByCurrentComp", fdid);

		return list;
	}

	@Override
	public List<CommonModel> getChildCompsByThisComp(String fdid) {
		List<CommonModel> list = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getChildCompsByThisComp", fdid);

		return list;
	}

	@Override
	public List<CommonModel> getCompanyList(CommonModel c) {
		List<CommonModel> list = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getCompanyList", c);
		list.parallelStream().filter(t -> {
			return t.getCompany_name().contains(c.getKeyWords());
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	public List<CommonModel> getDataDictList(CommonModel c) {
		List<CommonModel> list = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getDataDictList", c);

		return list;
	}

	@Override
	public JSONObject getOperationsByMenuId(CommonModel c) {
		JSONObject json = new JSONObject();
		List<CommonModel> list_auth = getAuthortiesByStaff(c);
		List<String> authIds = list_auth.stream().map(CommonModel::getFdid)
				.collect(Collectors.toList());
		c.setTemp_list(authIds);
		List<CommonModel> list = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getOperationsByMenuId", c);
		if (list.size() < 1) {
			json.put("res", "1");
			json.put("msg", "无按钮操作权限，请与管理员联系申请");
			return json;
		}
		json.put("res", "2");
		json.put("msg", "有按钮操作权限");
		JSONArray array = new JSONArray();
		for (CommonModel cc : list) {
			JSONObject json2 = new JSONObject();
			json2.put("id", cc.getFdid());
			json2.put("operate_code", cc.getOperate_code());
			json2.put("operate_name", cc.getOperate_name());
			json2.put("operate_type", cc.getOperate_type());
			json2.put("operate_css", cc.getOperate_css());
			json2.put("operate_address", cc.getAddress());
			json2.put("operate_target", cc.getOperate_target());
			json2.put("menuId", cc.getForeign_id());
			array.add(json2);
		}
		json.put("list", array);
		return json;
	}

	@Override
	public List<CommonModel> getAuthortiesByStaff(CommonModel c) {
		List<CommonModel> list = (ArrayList<CommonModel>) commonDao
				.getListData("systemSql.getAuthortiesByStaff", c);

		return list;
	}

	@Override
	public List<CommonModel> getDictInfo(CommonModel c) {
		CommonModel ccc = (CommonModel) commonDao.getOneData(
				"systemSql.getDictType", c);
		c.setParent_id(ccc.getFdid());
		List<CommonModel> list = (List<CommonModel>) commonDao.getListData(
				"systemSql.getDictInfo", c);
		return list;
	}

	@Override
	public void createCompany(CommonModel c) throws RbackException {
		// TODO Auto-generated method stub
		try {
			commonDao.insert("systemSql.createCompany", c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RbackException("1", e.getLocalizedMessage());
		}
	}

	@Override
	public void fillCommonField(BaseModel c, HttpSession session,
			boolean useFdid) {
		// TODO Auto-generated method stub
		if (useFdid) {
			c.setFdid(getUUID());
		}
		c.setCreate_company_id(((LogUser) session.getAttribute("LogUser"))
				.getCompany_id());
		c.setStaff_id(((LogUser) session.getAttribute("LogUser")).getUser_id());
		c.setAction_name(Thread.currentThread().getStackTrace()[1]
				.getMethodName());
		c.setVersion_time(getSysDate());
		c.setCreate_time(getSysDate());
	}

	@Override
	public List<CommonModel> getuserList(CommonModel c) {
		List<CommonModel> list = (List<CommonModel>) commonDao.getListData(
				"systemSql.getuserList", c);
		list.parallelStream()
				.filter(t -> {
					return t.getStaff_name().contains(c.getKeyWords())
							|| t.getCompany_type_name().contains(
									c.getKeyWords())
							|| t.getCompany_address().contains(c.getKeyWords());
				}).collect(Collectors.toList());
		return list;
	}

	@Override
	public List<CommonModel> getstaffGrpList(CommonModel c) {
		List<CommonModel> list = (List<CommonModel>) commonDao.getListData(
				"systemSql.getstaffGrpList", c);
		list.parallelStream().filter(t -> {
			return t.getGroup_name().contains(c.getKeyWords());
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	public List<CommonModel> getroleList(CommonModel c) {
		List<CommonModel> list = (List<CommonModel>) commonDao.getListData(
				"systemSql.getroleList", c);
		list.parallelStream().filter(t -> {
			return t.getRole_name().contains(c.getKeyWords());
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	public List<CommonModel> getpostionList(CommonModel c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CommonModel> getCommonList(CommonModel c) {
		List<CommonModel> list = (List<CommonModel>) commonDao.getListData(
				c.getSql(), c);
		return list;
	}

	@Override
	public CommonModel getOneCommnonData(CommonModel c) {
		CommonModel cc = (CommonModel) commonDao.getOneData(c.getSql(), c);
		return cc;
	}

	@Override
	public void insert(CommonModel c) throws RbackException {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		try {
			commonDao.insert(c.getSql(), c);
		} catch (RbackException e) {
			throw new RbackException("1", e.getMsg());
		}

	}

	@Override
	public void update(CommonModel c) throws RbackException {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		try {
			commonDao.update(c.getSql(), c);
		} catch (RbackException e) {
			throw new RbackException("1", e.getMsg());
		}
	}

	@Override
	public void delete(CommonModel c) throws RbackException {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		try {
			commonDao.delete(c.getSql(), c);
		} catch (RbackException e) {
			throw new RbackException("1", e.getMsg());
		}
	}

	@Override
	public List<CommonModel> getObjByKeyWds(CommonModel c, String objStr) {
		/***
		 * 根据查询类型，
		 */
		List<CommonModel> listTotal = new ArrayList<>();
		c.setSql("systemSql.getStaffInfoList");
		List<CommonModel> list_staff = getCommonList(c);
		c.setSql("systemSql.getStaffGrpList");
		List<CommonModel> list_grp = getCommonList(c);
		c.setSql("systemSql.getPostionList");
		List<CommonModel> list_post = getCommonList(c);
		c.setSql("systemSql.getRoleList");
		List<CommonModel> list_role = getCommonList(c);
		c.setSql("systemSql.getCompList");
		List<CommonModel> list_comp = getCommonList(c);
		String[] array = objStr.split("");
		if ("1".equals(array[0])) {
			listTotal.addAll(list_staff);
		}
		if ("1".equals(array[1])) {
			listTotal.addAll(list_grp);
		}
		if ("1".equals(array[2])) {
			listTotal.addAll(list_post);
		}
		if ("1".equals(array[3])) {
			listTotal.addAll(list_role);
		}
		if ("1".equals(array[4])) {
			listTotal.addAll(list_comp);
		}
		return listTotal;
	}

	/*****
	 * 
	 */
	@Override
	public void insertTempRelation(CommonModel c) throws RbackException {
		// TODO Auto-generated method stub
		c.setSql("systemSql.getTempRelation");
		List<CommonModel> ls = commonDao.getListData(c.getSql(), c);
		if (ls.size() > 0) {
			c.setSql("systemSql.delTempRelation");
			commonDao.delete(c.getSql(), c);
		}
		c.setSql("systemSql.insertTempRelation");
		commonDao.insert(c.getSql(), c);
	}

	/****
	 * 创建一个用户
	 */
	@Override
	public void createStaff(Staff staff) throws RbackException {
		// TODO Auto-generated method stub
		staff.setPasswd(getPasswdMMOfStaff(staff.getStaff_number(),
				staff.getPasswd(),
				PropertiesUtil.getValueByKey("user.pw.slogmm")));
		staff.setSql("systemSql.createNewStaff");
		commonDao.insert(staff.getSql(), staff);
		String[] array_grp = staff.getStaff_group_id().split(
				PropertiesUtil.getValueByKey("system.delimiter"));
		for (int i = 0; i < array_grp.length; i++) {
			staff.setStaff_group_id(array_grp[i]);
			staff.setSql("systemSql.createStaffGrp");
			commonDao.insert(staff.getSql(), staff);
		}
		staff.setSql("systemSql.createStaffComp");
		commonDao.insert(staff.getSql(), staff);
		String[] array_role = staff.getRole_id().split(
				PropertiesUtil.getValueByKey("system.delimiter"));
		for (int i = 0; i < array_role.length; i++) {
			staff.setRole_id(array_role[i]);
			staff.setSql("systemSql.createStaffRole");
			commonDao.insert(staff.getSql(), staff);
		}
		String[] array_post = staff.getPostion_id().split(
				PropertiesUtil.getValueByKey("system.delimiter"));
		for (int i = 0; i < array_post.length; i++) {
			staff.setPostion_id(array_post[i]);
			staff.setSql("systemSql.createStaffPost");
			commonDao.insert(staff.getSql(), staff);
		}
	}

	@Override
	public String getPasswdMMOfStaff(String staff_number, String password,
			String header) {
		MD5 m = new MD5();
		StringBuffer smm = new StringBuffer();
		smm.append(PropertiesUtil.getValueByKey("user.pw.slogmm"));
		smm.append(staff_number);
		smm.append(password);
		return m.getMD5ofStr(smm.toString());
	}

	@Override
	public void updateStaff(Staff staff) throws RbackException {
		// TODO Auto-generated method stub
		staff.setSql("systemSql.updateStaff");
		commonDao.update(staff.getSql(), staff);
		// 是否有组织
		staff.setSql("systemSql.getStaffComp");
		List list = commonDao.getListData(staff.getSql(), staff);
		if (list.size() > 0) { // 如果有先删除
			staff.setSql("systemSql.delStaffComp");
			commonDao.delete(staff.getSql(), staff);
		}
		staff.setSql("systemSql.createStaffComp");
		commonDao.insert(staff.getSql(), staff);
		// 是否有角色
		staff.setSql("systemSql.getStaffRole");
		list = commonDao.getListData(staff.getSql(), staff);
		if (list.size() > 0) { // 如果有先删除
			staff.setSql("systemSql.delStaffRole");
			commonDao.delete(staff.getSql(), staff);
		}
		String[] array_role = staff.getRole_id().split(
				PropertiesUtil.getValueByKey("system.delimiter"));
		for (int i = 0; i < array_role.length; i++) {
			staff.setRole_id(array_role[i]);
			staff.setSql("systemSql.createStaffRole");
			commonDao.insert(staff.getSql(), staff);
		}
		// 是否有用户组
		staff.setSql("systemSql.getStaffGrps");
		list = commonDao.getListData(staff.getSql(), staff);
		if (list.size() > 0) { // 如果有先删除
			staff.setSql("systemSql.delStaffGrps");
			commonDao.delete(staff.getSql(), staff);
		}
		String[] array_grp = staff.getStaff_group_id().split(
				PropertiesUtil.getValueByKey("system.delimiter"));
		for (int i = 0; i < array_grp.length; i++) {
			staff.setStaff_group_id(array_grp[i]);
			staff.setSql("systemSql.createStaffGrp");
			commonDao.insert(staff.getSql(), staff);
		}
		// 是否有岗位
		staff.setSql("systemSql.getStaffPost");
		list = commonDao.getListData(staff.getSql(), staff);
		if (list.size() > 0) { // 如果有先删除
			staff.setSql("systemSql.delStaffPost");
			commonDao.delete(staff.getSql(), staff);
		}
		String[] array_post = staff.getPostion_id().split(
				PropertiesUtil.getValueByKey("system.delimiter"));
		for (int i = 0; i < array_post.length; i++) {
			staff.setPostion_id(array_post[i]);
			staff.setSql("systemSql.createStaffPost");
			commonDao.insert(staff.getSql(), staff);
		}

	}

	@Override
	public Map<String, CommonModel> getRlPostGrpInfosByStaffId(String staff_id) {
		// TODO Auto-generated method stub
		Map<String, CommonModel> return_map = new HashMap<>();
		StringBuffer ids = new StringBuffer();
		StringBuffer names = new StringBuffer();
		CommonModel c = new CommonModel();
		c.setFdid(staff_id);
		// 角色部分
		c.setSql("systemSql.getRoleInfoByStaff");
		List<CommonModel> list = commonDao.getListData(c.getSql(), c);
		for (CommonModel cc : list) {
			ids.append(cc.getFdid());
			ids.append(PropertiesUtil.getValueByKey("system.delimiter"));
			names.append(cc.getTemp_str1());
			names.append(PropertiesUtil.getValueByKey("system.delimiter"));
		}
		CommonModel cc3 = new CommonModel();
		cc3.setTemp_str1(ids.toString());
		cc3.setTemp_str2(names.toString());
		return_map.put("role", cc3);
		ids = new StringBuffer();
		names = new StringBuffer();
		// 用户组部分
		c.setSql("systemSql.getGrpInfoByStaff");
		List<CommonModel> listw = commonDao.getListData(c.getSql(), c);
		for (CommonModel cc : listw) {
			ids.append(cc.getFdid());
			ids.append(PropertiesUtil.getValueByKey("system.delimiter"));
			names.append(cc.getTemp_str1());
			names.append(PropertiesUtil.getValueByKey("system.delimiter"));
		}
		CommonModel cc4 = new CommonModel();
		cc4.setTemp_str1(ids.toString());
		cc4.setTemp_str2(names.toString());
		return_map.put("staff_group", cc4);
		ids = new StringBuffer();
		names = new StringBuffer();
		// 岗位部分
		c.setSql("systemSql.getPostInfoByStaff");
		List<CommonModel> list2 = commonDao.getListData(c.getSql(), c);
		for (CommonModel cc : list2) {
			ids.append(cc.getFdid());
			ids.append(PropertiesUtil.getValueByKey("system.delimiter"));
			names.append(cc.getTemp_str1());
			names.append(PropertiesUtil.getValueByKey("system.delimiter"));
		}
		CommonModel cc5 = new CommonModel();
		cc5.setTemp_str1(ids.toString());
		cc5.setTemp_str2(names.toString());
		return_map.put("postion", cc5);

		return return_map;
	}

	@Override
	public void createStaffGrp(CommonModel c) throws RbackException {
		// TODO Auto-generated method stub
		c.setSql("systemSql.createStaffGrp2D");
		commonDao.insert(c.getSql(), c);
		String[] array = c.getTemp_str1().split(
				PropertiesUtil.getValueByKey("system.delimiter"));
		for (String s : array) {
			CommonModel cc = new CommonModel();
			cc.setTemp_str1(s);
			cc.setFdid(c.getFdid());
			cc.setSql("systemSql.createGrp2Staffs");
			commonDao.insert(cc.getSql(), cc);
		}
	}

	/**
	 * 删除用户组
	 */
	@Override
	public void deleteStaffGroup(CommonModel c) throws RbackException {
		// TODO Auto-generated method stub
		c.setSql("systemSql.deleteStaffGroup");
		commonDao.update(c.getSql(), c);
	}

	@Override
	public CommonModel getDetailsOfStaffGrp(CommonModel c) {
		// TODO Auto-generated method stub
		CommonModel cc = new CommonModel();
		cc.setFdid(c.getFdid());
		StringBuffer ids = new StringBuffer();
		StringBuffer names = new StringBuffer();
		cc.setSql("systemSql.getDetailsOfStaffGrp");
		List<CommonModel> list = commonDao.getListData(cc.getSql(), cc);
		for (CommonModel m : list) {
			ids.append(m.getStaff_id());
			ids.append(PropertiesUtil.getValueByKey("system.delimiter"));
			names.append(m.getStaff_name());
			names.append(PropertiesUtil.getValueByKey("system.delimiter"));
		}
		cc.setGroup_name(list.get(0).getGroup_name());
		cc.setTemp_str1(ids.toString());
		cc.setTemp_str2(names.toString());
		return cc;
	}

	@Override
	public void updateStaffGrp(CommonModel c) throws RbackException {
		// TODO Auto-generated method stub
		c.setSql("systemSql.updateStaffGrp2D");
		commonDao.insert(c.getSql(), c);
		c.setSql("systemSql.delGrp2Staffs");
		commonDao.delete(c.getSql(), c);
		String[] array = c.getTemp_str1().split(
				PropertiesUtil.getValueByKey("system.delimiter"));
		for (String s : array) {
			CommonModel cc = new CommonModel();
			cc.setTemp_str1(s);
			cc.setFdid(c.getFdid());
			cc.setSql("systemSql.createGrp2Staffs");
			commonDao.insert(cc.getSql(), cc);
		}

	}

	@Override
	public JSONObject createPostion(CommonModel c) throws RbackException {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		c.setSql("systemSql.getPostionNameExiste");
		List list = commonDao.getListData(c.getSql(), c);
		if (list.size() > 0) {
			json.put("res", "1");
			json.put("msg", "此名称已经被占用");
			return json;
		}
		c.setSql("systemSql.createPostion");
		commonDao.insert(c.getSql(), c);
		json.put("res", "2");
		json.put("msg", "执行成功");
		return json;
	}

	@Override
	public void delPostion(CommonModel c) throws RbackException {
		// TODO Auto-generated method stub
		c.setSql("systemSql.delPostion");
		commonDao.delete(c.getSql(), c);
		c.setSql("systemSql.getPostionRelation");
		if (commonDao.getListData(c.getSql(), c).size() > 0) {
			c.setSql("systemSql.delPostionRelation");
			commonDao.delete(c.getSql(), c);
		}
	}

	@Override
	public CommonModel getDetailsOfPostion(CommonModel c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePostion(CommonModel c) throws RbackException {
		// TODO Auto-generated method stub
		commonDao.update("systemSql.updatePostion", c);
	}

	@Override
	public List<CommonModel> getAuthTreeAll(CommonModel c) {
		// TODO Auto-generated method stub
		c.setSql("systemSql.getAuthTreeAll");
		List<CommonModel> all_list = commonDao.getListData(c.getSql(), c);
		return all_list;
	}

	@Override
	public List<CommonModel> getAuthTreeOfRole(CommonModel c) {
		// TODO Auto-generated method stub
		c.setSql("systemSql.getMyAuthIdsOfRoles");
		List<CommonModel> myIds = commonDao.getListData(c.getSql(), c);
		List<String> list_str = myIds.stream().map(CommonModel::getFdid)
				.collect(Collectors.toList());

		CommonModel cc = new CommonModel();
		cc.setTemp_list(list_str);
		cc.setSql("systemSql.getAuthTreeOfMe");
		List<CommonModel> treeList = commonDao.getListData(cc.getSql(), cc);
		return treeList;
	}

	@Override
	public JSONObject getAuthTreeOfMyRole(CommonModel c) {
		JSONObject json = new JSONObject();
		List<CommonModel> list = getAuthTreeOfRole(c);
		if (list.size() < 1) {
			json.put("res", "1");
			json.put("msg", "无权限");
			return json;
		}
		json.put("res", "2");
		json.put("msg", "存在权限");
		JSONArray array = new JSONArray();
		for (CommonModel m : list) {
			JSONObject j = new JSONObject();
			j.put("id", m.getFdid());
			j.put("name", m.getTemp_str1());
			j.put("t", m.getTemp_str1());
			j.put("pId", m.getParent_id());
			j.put("open", false);
			array.add(j);
		}
		json.put("list", array);
		return json;
	}

	@Override
	public boolean hasTopRoleSysStaff(HttpSession session) {
		// TODO Auto-generated method stub
		List<Role> list = ((LogUser) session.getAttribute("LogUser"))
				.getRoles();
		if (list != null && list.size() > 0) {
			for (Role r : list) {
				if (PropertiesUtil.getValueByKey("system.delimiter").equals(
						r.getFdid())) {
					return true; // 说明当前这个用户有最高权限的角色
				}
			}
		}
		return false;
	}

}
