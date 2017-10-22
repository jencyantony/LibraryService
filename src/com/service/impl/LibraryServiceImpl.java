package com.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

import com.service.bo.LoginInfoBO;
import com.service.bo.UserInfoBO;
import com.service.bo.UserRegistrationInfoBO;
import com.service.dao.BookDAO;
import com.service.dao.LoginInfoDAO;
import com.service.dao.UserInfoDAO;
import com.service.exception.LibraryServiceException;

public class LibraryServiceImpl {

	public String registerUser(UserRegistrationInfoBO user) throws LibraryServiceException{
		try{
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.configure();
			SessionFactory sessionFactory = cfg.buildSessionFactory();
			Session session = sessionFactory.openSession();
			
			UserInfoDAO newUser = new UserInfoDAO();
			newUser.setFirstname(user.getFirstname());
			newUser.setLastname(user.getLastname());
			newUser.setDepartment(user.getDepartment());
			newUser.setRole(user.getRole());
			newUser.setId(user.getId());
			
			LoginInfoDAO loginInfo = new LoginInfoDAO();
			loginInfo.setId(user.getId());
			loginInfo.setUsername(user.getUsername());
			loginInfo.setPassword(user.getPassword());
			
			session.beginTransaction();
			session.save(newUser);
			session.save(loginInfo);
			session.getTransaction().commit();
			session.close();
			return "Registered";
		} catch(Exception e){
			throw new LibraryServiceException(e.getMessage());
		}
	}
	
	public boolean isValidUser(LoginInfoBO loginInfo) throws LibraryServiceException{
		try{
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.configure();
			SessionFactory sessionFactory = cfg.buildSessionFactory();
			Session session = sessionFactory.openSession();
			
			LoginInfoDAO infoFromDB = (LoginInfoDAO)session.get(LoginInfoDAO.class, loginInfo.getUsername());
			session.beginTransaction().commit();
			session.close();
			if(infoFromDB!=null && infoFromDB.getPassword().equals(loginInfo.getPassword())){
				return true;
			}
			return false;
		} catch(Exception e){
			throw new LibraryServiceException(e.getMessage());
		}
	}
	
	public UserInfoBO getUserDetails(int userId) throws LibraryServiceException{
		try{
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.configure();
			SessionFactory sessionFactory = cfg.buildSessionFactory();
			Session session = sessionFactory.openSession();
			
	        UserInfoDAO userInfoDao = (UserInfoDAO)session.get(UserInfoDAO.class, userId);
			UserInfoBO userInfoBo = new UserInfoBO();
	        userInfoBo.setFirstname(userInfoDao.getFirstname());
	        userInfoBo.setLastname(userInfoDao.getLastname());
	        userInfoBo.setDepartment(userInfoDao.getDepartment());
	        userInfoBo.setRole(userInfoDao.getRole());
	        userInfoBo.setId(userInfoDao.getId());
			Query query = session.createQuery("from book_details b where b.borrowedBy = :borrowedBy");
	        query.setParameter("borrowedBy", userId);
	        List books = query.list();
	        BookDAO[] booksBorrowed = new BookDAO[books.size()];
	        ArrayList<BookDAO> booksBorrowedList = new ArrayList<BookDAO>();
	        for(Object book:books){
	        	booksBorrowedList.add((BookDAO)book);
	        }
	        booksBorrowedList.toArray(booksBorrowed);
	        userInfoBo.setBooksBorrowed(booksBorrowed);
			session.beginTransaction().commit();
			session.close();
			return userInfoBo;
		} catch(Exception e){
			throw new LibraryServiceException(e.getMessage());
		}
	}
	
}