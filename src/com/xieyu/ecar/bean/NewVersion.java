package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * fir 更新
 * 
 * @author fanbaolong
 *
 */
public class NewVersion implements Serializable
{

	private static final long serialVersionUID = -2544385162841969337L;

	private String name;
	private int version;
	private String changelog;
	private String updated_at;
	private String versionShort;
	private int build;
	private String installUrl;
	private String install_url;
	private String direct_install_url;
	private String update_url;
	private Binary binary;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public String getChangelog()
	{
		return changelog;
	}

	public void setChangelog(String changelog)
	{
		this.changelog = changelog;
	}

	public String getUpdated_at()
	{
		return updated_at;
	}

	public void setUpdated_at(String updated_at)
	{
		this.updated_at = updated_at;
	}

	public String getVersionShort()
	{
		return versionShort;
	}

	public void setVersionShort(String versionShort)
	{
		this.versionShort = versionShort;
	}

	public int getBuild()
	{
		return build;
	}

	public void setBuild(int build)
	{
		this.build = build;
	}

	public String getInstallUrl()
	{
		return installUrl;
	}

	public void setInstallUrl(String installUrl)
	{
		this.installUrl = installUrl;
	}

	public String getInstall_url()
	{
		return install_url;
	}

	public void setInstall_url(String install_url)
	{
		this.install_url = install_url;
	}

	public String getDirect_install_url()
	{
		return direct_install_url;
	}

	public void setDirect_install_url(String direct_install_url)
	{
		this.direct_install_url = direct_install_url;
	}

	public String getUpdate_url()
	{
		return update_url;
	}

	public void setUpdate_url(String update_url)
	{
		this.update_url = update_url;
	}

	public Binary getBinary()
	{
		return binary;
	}

	public void setBinary(Binary binary)
	{
		this.binary = binary;
	}

	private class Binary
	{
		private String fsize;

		public String getFsize()
		{
			return fsize;
		}

		public void setFsize(String fsize)
		{
			this.fsize = fsize;
		}

	}

}
