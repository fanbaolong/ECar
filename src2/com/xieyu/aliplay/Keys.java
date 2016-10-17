/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.xieyu.aliplay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys
{

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088121656893580";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "vencent.n@outlook.com";

	// 商户（RSA）私钥(注意一定要转PKCS8格式，否则在Android4.0及以上系统会支付失败)
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAO7j0lAOwnNEB5EWLPgjvtnHgROij0HUT7c71QpDuFccGgjb8bRaVvVAaMn6zuxD1dlrDcxNEfYcY8uQCr1+3DycOuVwWOe8JApH7NqrogkDdetmhmGBwE4e5TaSsMUn4wrN33obdxy9UpZ/uR45qh+Usy6SaCRcUWrN8LJXTZU/AgMBAAECgYEAjK/W+Bb5JXD3E9b/fNNwXklEFxAxVg93Mu4ltPnU4lhGbNk7K3/Ynm7kR69RlYsNshF9+seYwFxP2V5nNNo1UMlBBGKiKouUWaVBKCSlPBhYz9Iik1cT/ACgrWLh9dZ9Ss9alCGiVXeQRRYxtFBVP/rqsVsqWjpZ0Xgu/U+8WkECQQD9uRdxxlKqf5HOBZ44vsBTfEyaPTzw8Mk2iGZIXIzoHyhzipCN0w5y0m3mr5a2N8vs74TderF8iECp21eqfFBXAkEA8Qim8VfVlNH9d3hn5R9K7pv9jE2QUxp53doHG1iy358oBQZG8R3d4qwd1hf45P3aP0sGBe3qw6LGbFn7CdMxWQJAfj53hNv6S+2Xvdwbwv5eseuaLfCkfvpAFAtAYWbzkZfXR849aOQIP2em2W6q3dXbePQgtxcScvVUv29tKl4alwJAFUp7XKQylJO7/Pqgysdp2gvjaKpNTlnvW95vdEXwfvpTEaaNS60ktWoYtJmnfarCxYZ+ALYUX7WYK2gDWcnS+QJBAND6lDMkLippjFhxm58sm0quZS6DZJDHDaShbe7uzvt3BZNxGlT3zB/cQUYmaH4ZtQhqE759Ovq8fLKXaCjKXFQ=";

	// 支付宝（RSA）公钥用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	// 9szgr894jl15iaqi1e4hjdvz275z99j6
}
