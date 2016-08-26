package com.vc.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.vc.entity.account.User;

public class PasswordHelper {
	
	private final static class SingleHolder {
		private static PasswordHelper INSATANCE = new PasswordHelper();
	}
	
	public static PasswordHelper getInstance() {
		return SingleHolder.INSATANCE;
	}
	
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private String algorithmName = "md5";
	private final int hashIterations = 2;

	public void encryptPassword(User user) {
		// 加密方式要和配置文件中配置的方式相一致
		user.setSalt(randomNumberGenerator.nextBytes().toHex());
		String newPassword = new SimpleHash(algorithmName, user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()),
				hashIterations).toHex();
		user.setPassword(newPassword);
	}
	
}