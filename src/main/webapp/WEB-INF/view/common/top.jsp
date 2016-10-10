<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="navbar-header pull-left">
					<a href="javascript:;" class="navbar-brand">
						<small>
							<i class="fa fa-leaf"></i>
							后台管理系统
						</small>
					</a>
				</div>
				<div class="navbar-buttons navbar-header pull-right" role="navigation">
					<ul class="nav ace-nav">
						<!-- #section:basics/navbar.user_menu -->
						<li class="light-blue">
							<a data-toggle="dropdown" href="#" class="dropdown-toggle">
								<!--  img class="nav-user-photo" src="${res_url}ace-1.3.3/assets/avatars/user.jpg" alt="Jason's Photo" /-->
								<span class="user-info">
									<small>Welcome,</small>
									${sessionScope.sysUser.name}
								</span>

								<i class="ace-icon fa fa-caret-down"></i>
							</a>

							<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
								<li>
									<a href="javascript:;" id="pwd-update">
										<i class="ace-icon fa fa-cog"></i>
										密码设置
									</a> 
								</li>
								<li class="divider"></li>
								<li>
									<a href="${context_path}/loginOut">
										<i class="ace-icon fa fa-power-off"></i>
										退出
									</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
				