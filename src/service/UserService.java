package service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dao.IUserDAO;
import dao.impl.DAOFactory;
import model.User;

public class UserService {
	
	private static UserService INSTANCE = new UserService();
	private IUserDAO userDAO = DAOFactory.getUserDao();
	
	private UserService() {}
	public static UserService getInstance() {
		return INSTANCE;
	}
	
	public boolean isLoginAvailable(String login) {
		boolean available = false;
		
		if (userDAO.findByLogin(login) == null) {
			available = true;
		}
		return available;
	}
	
	public void createUser (User u) {
		if (isLoginAvailable(u.getLogin())) {
			u.setPwd(getMd5(u.getPwd()));
			try {
				userDAO.create(u);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void doLogin(String login, String pwd) throws Exception {
		User u = userDAO.findByLogin(login);
		String md5ToVerify = getMd5(pwd);
		
		if (u == null || !md5ToVerify.equals(u.getPwd())) {
			throw new Exception("Wrong login and/or password");
		}
		
		System.out.println("User logged : " + u.getFirstname() + " " + u.getLastname());
	}
	
	private String getMd5(String input) {
		try {
			  
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
  
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
  
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
  
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
            
         // For specifying wrong message digest algorithms
			} catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
	}

}
