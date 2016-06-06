package it.tecnosphera.booking.classroom.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.tecnosphera.booking.classroom.model.User;

public class UserRepository implements UserRepositoryInterface {

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
	      // Setup the connection with the DB
	    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/prenotazioni?user=root");
	    return connection;
	}
	
	@Override
	public User getUser(long id) {
		User user = null;
		Connection conn = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE id='" + id + "'");
			if (rs.next()) {
				String name = rs.getString("Nome");
				String cognome = rs.getString("Cognome");
				String email = rs.getString("Email");
				String password = rs.getString("Password");
				
				
				user = createUser(id, name,cognome,email,password);
			}

		} catch (ClassNotFoundException e) {
			
		} catch (SQLException e) {
			
		} finally {
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return user;
	}
	
	protected User createUser(long id, String nome,String cognome,String email,String password) {
		User user = new User();
		user.setId(id);
		user.setName(nome);
		user.setCognome(cognome);
		user.setEmail(email);
		user.setPassword(password);
		return user;
	}

	@Override
	public List<User> getUsers() {
		
		List<User> lista = new ArrayList<User>();
		Connection conn = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
		
			String qry = "SELECT * FROM user";

			ResultSet res = stmt.executeQuery(qry);
			while (res.next()) {
				long id = res.getLong("id");
				String nome = res.getString("nome");
				String cognome = res.getString("Cognome");
				String email = res.getString("Email");
				String password = res.getString("Password");
				
				User user = createUser(id, nome, cognome, email, password);
				lista.add(user);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return lista;
	}
		

	
	@Override
	public List<User> getUsers(String name) {
		// TODO Auto-generated method stub
		return null;
	}		
	
	public long insertUser (User user){
		
		Connection conn = null;
		long key = -1L;
		
		PreparedStatement preparedStatement = null;
		try {
			conn = getConnection();
			
			String sql =("insert into user (nome) values (?),(cognome) values (?),(email) values (?),(password) values (?)");
			preparedStatement = conn
			          .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getCognome());
			preparedStatement.setString(3, user.getEmail());
			preparedStatement.setString(4, user.getPassword());
			
						
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs != null && rs.next()) {
			    key = rs.getLong(1);
			}
			
			

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return key;
		
		
	}
	
	public long updateUser (User user){
		return 0;
	}

	@Override
	public long save(User user) {
		
		if (user.getId()==0)
			insertUser(user);
		else
			updateUser(user);
		return 0;
		
	
	}

	@Override
	public boolean delete(User user) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
