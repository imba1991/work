import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ht.UserCenterStartBootApplication;
import com.ht.business.user.dto.UserInfoDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author qiujunda
 * @date 2019年1月3日
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=UserCenterStartBootApplication.class)
public class UserInfoBaseControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockHttpSession session;

	@Autowired
	private MockHttpServletRequest request;

	@Before
	public void setip() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

//	@Test
//	@Rollback
//	@Transactional
//	public void testUpdatePasswd() throws Exception {
////		Map<String, Object> map = new HashMap<String, Object>();
////		map.put("account", "admin");
////		map.put("newPasswd", "123456");
////		map.put("oldPasswd", "123");
////		MvcResult result = mockMvc.perform(post("/user/base/updatePasswd").content(JSONObject.toJSONString(map)))
////				.andExpect(status().isOk())// 模拟向testRest发送get请求
//////				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
////				.andReturn();// 返回执行请求的结果
//		
//		MvcResult result = mockMvc.perform(post("/user/base/updatePasswd").
//				param("account", "admin").param("newPasswd", "123").param("oldPasswd", "1234"))
//				.andExpect(status().isOk())// 模拟向testRest发送get请求
////				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
//				.andReturn();// 返回执行请求的结果
//		String resultStr=result.getResponse().getContentAsString();
//		System.out.println("testUpdatePasswd--------------"+resultStr);
//		Map resultMap=(Map) JSON.parse(resultStr);
//		int code=(int)resultMap.get("code");
//		Assert.assertTrue("成功", code==1001||code==5001);
//	}
	@Test
	@Rollback
	@Transactional
	public void test1User()throws Exception {
		UserInfoDTO dto=new UserInfoDTO();
		dto.setId("test");
		dto.setPassWord("123");
		dto.setUserName("邱俊达");
		MvcResult result = mockMvc.perform(post("/user/base/add")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject.toJSONString(dto)))
				.andExpect(status().isOk())// 模拟向testRest发送get请求
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
				.andReturn();// 返回执行请求的结果
		String resultStr=result.getResponse().getContentAsString();
		System.out.println("testAddUser--------------"+resultStr);
		Map resultMap=(Map) JSON.parse(resultStr);
		int code=(int)resultMap.get("code");
		Assert.assertTrue("成功", code==1001||code==5001);
		
		dto.setHomePhone("18615705593");
		
		result = mockMvc.perform(post("/user/base/update")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(JSONObject.toJSONString(dto)))
				.andExpect(status().isOk())// 模拟向testRest发送get请求
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
				.andReturn();// 返回执行请求的结果
		resultStr=result.getResponse().getContentAsString();
		System.out.println("testUpdateUser--------------"+resultStr);
		resultMap=(Map) JSON.parse(resultStr);
		code=(int)resultMap.get("code");
		Assert.assertTrue("成功", code==1001||code==5001);
		
		result = mockMvc.perform(post("/user/base/updatePasswd").
				param("account", dto.getId()).param("newPasswd", "1234567").param("oldPasswd", dto.getPassWord()))
				.andExpect(status().isOk())// 模拟向testRest发送get请求
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
				.andReturn();// 返回执行请求的结果
		resultStr=result.getResponse().getContentAsString();
		System.out.println("testUpdatePasswd--------------"+resultStr);
		resultMap=(Map) JSON.parse(resultStr);
		code=(int)resultMap.get("code");
		Assert.assertTrue("成功", code==1001||code==5001);
		
		result = mockMvc.perform(post("/user/base/delete")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("[\""+dto.getId()+"\"]"))
				.andExpect(status().isOk())// 模拟向testRest发送get请求
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
				.andReturn();// 返回执行请求的结果
		resultStr=result.getResponse().getContentAsString();
		System.out.println("testDeleteUser--------------"+resultStr);
		resultMap=(Map) JSON.parse(resultStr);
		code=(int)resultMap.get("code");
		Assert.assertTrue("成功", code==1001||code==5001);
	}
}
