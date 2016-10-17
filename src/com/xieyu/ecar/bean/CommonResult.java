package com.xieyu.ecar.bean;

/**
 * @author fanbaolong
 *
 *         接口统一返回类型
 */
public class CommonResult
{
	/**
	 * 类型
	 */
	public enum Type
	{
		/** 正确 */
		OK,

		/** 已经存在 */
		IsExist,

		/** 验证失败 */
		ValidationFailed,

		/** 参数非法 */
		IllegalParameter,

		/** 数据库异常 */
		SQLException,

		/** 失败 */
		Error,

		/** 超时 ，重新登陆 */
		SESSIONOUT,
	}

	// /上面的枚举类型，状态
	private Type resultType;

	// /返回内容
	private String resultMes;

	// /多封装了一个返回值对象
	public class ClientResult<T> extends CommonResult
	{
		// /返回的对象
		private T objectResult;
	}

	public Type getResultType()
	{
		return resultType;
	}

	public void setResultType(Type resultType)
	{
		this.resultType = resultType;
	}

	public String getResultMes()
	{
		return resultMes;
	}

	public void setResultMes(String resultMes)
	{
		this.resultMes = resultMes;
	}

}
