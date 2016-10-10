/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fruit.core.cache;

import com.jfinal.plugin.ehcache.CacheKit;

public final class CacheClearUtils {
	/**
	 * 清除所有用户的资源缓存
	 */
	public static void clearUserMenuCache(){
		CacheKit.removeAll(CacheName.userMenuCache);
	}
	
	public static void clearDictCache(){
		CacheKit.removeAll(CacheName.dictCache);
	}
}
