package cn.enilu.flash.api.controller.test;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.entity.test.Girl;
import cn.enilu.flash.service.test.GirlService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.factory.Page;


import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test/girl")
public class GirlController extends BaseController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private GirlService girlService;

	@GetMapping(value = "/list")
	@RequiresPermissions(value = "girl")
	public Ret list(@RequestParam(required = false) Long id) {
		Page<Girl> page = new PageFactory<Girl>().defaultPage();
		page.addFilter("id",id);
		page = girlService.queryPage(page);
		return Rets.success(page);
	}
	@PostMapping
	@BussinessLog(value = "新增女孩", key = "name")
	@RequiresPermissions(value = "girlAdd")
	public Ret add(@RequestBody Girl girl){
		girlService.insert(girl);
		return Rets.success();
	}
	@PutMapping
	@BussinessLog(value = "更新女孩", key = "name")
	@RequiresPermissions(value = "girlUpdate")
	public Ret update(@RequestBody Girl girl){
		girlService.update(girl);
		return Rets.success();
	}
	@DeleteMapping
	@BussinessLog(value = "删除女孩", key = "id")
	@RequiresPermissions(value = "girlDelete")
	public Ret remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		girlService.delete(id);
		return Rets.success();
	}
}