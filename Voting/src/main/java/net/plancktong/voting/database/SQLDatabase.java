package net.plancktong.voting.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool.PoolInitializationException;

import lombok.Cleanup;
import net.plancktong.voting.NameFetcher;
import net.plancktong.voting.database.credentials.SQLCredentials;
import net.plancktong.voting.model.Vote;

public class SQLDatabase implements Database<Vote> {

	private final static int MAXIMUM_POOLSIZE = 10;
	private final static int MAXIMUM_LIFETIME = 600000;
	private final static int IDLE_TIMEOUT = 45000;
	
	private HikariDataSource datasource;
	
	private SQLCredentials sqlCredentials;
	
	public SQLDatabase() {
		this.sqlCredentials = new SQLCredentials("localhost", "voting", "root", "");
	}
	
	public void setup() {
		if (datasource != null) return;
		
		try {
			HikariConfig config = new HikariConfig();
			
			config.setDriverClassName("com.mysql.jdbc.Driver");
			config.setJdbcUrl("jdbc:mysql://" + sqlCredentials.getHostName() + ":" + sqlCredentials.getPort() + "/" 
			+ sqlCredentials.getDatabaseName() + "?autoReconnect=true&useSSL=false");
			config.setUsername(sqlCredentials.getUsername());
			config.setPassword(sqlCredentials.getPassword());
	        config.setMaxLifetime(MAXIMUM_LIFETIME);
	        config.setIdleTimeout(IDLE_TIMEOUT);				
	        config.setMaximumPoolSize(MAXIMUM_POOLSIZE);
	        
			datasource = new HikariDataSource(config);
			System.out.println("Opened the database connections.");
		} catch (PoolInitializationException e) {
			System.out.println("================================");
			System.out.println("Could not connect to the database. Configure your config and restart the server!");
			System.out.println("================================");
		}
	}
	
	public void createTables() {
		try {
			@Cleanup Connection connection = datasource.getConnection();
			@Cleanup PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS voting"
	        + "  (id INT(10) AUTO_INCREMENT,"
	        + "   uuid VARCHAR(36) NOT NULL,"
	        + "   servicename VARCHAR(255) NOT NULL,"
	        + "   when_voted TIMESTAMP NOT NULL,"
	        + "   active TINYINT NOT NULL DEFAULT 1,"
	        + " CONSTRAINT voting_pk PRIMARY KEY (id))");
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Vote load(UUID uuid) {
		try {
			@Cleanup Connection connection = datasource.getConnection();
			@Cleanup PreparedStatement statement = connection.prepareStatement("SELECT * FROM voting WHERE uuid=?");
			
			statement.setString(1, uuid.toString());
			
			@Cleanup ResultSet rs = statement.executeQuery();
			
			if (!rs.next()) return null;
			
			return new Vote(uuid, rs.getString("servicename"), rs.getTimestamp("when_voted"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void save(Vote t) {
		try {
			@Cleanup Connection connection = datasource.getConnection();
			@Cleanup PreparedStatement statement = connection.prepareStatement("INSERT INTO voting(uuid, servicename, when_voted) VALUES(?,?,?)");
			statement.setString(1, t.getUuid().toString());
			statement.setString(2, t.getVotingSite());
			statement.setTimestamp(3, t.getWhenVoted());
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}	
	}

	public boolean remove(Vote t) {
		try {
			@Cleanup Connection connection = datasource.getConnection();
			@Cleanup PreparedStatement statement = connection.prepareStatement("DELETE FROM voting WHERE uuid=?");
			
			statement.setString(1, t.getUuid().toString());
			
			statement.execute();			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void resetAll() {
		try {
			@Cleanup Connection connection = datasource.getConnection();
			@Cleanup PreparedStatement statement = connection.prepareStatement("UPDATE voting SET active=?");
			
			statement.setInt(1, 0);
			
			statement.execute();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reset(UUID uuid) {
		try {
			@Cleanup Connection connection = datasource.getConnection();
			@Cleanup PreparedStatement statement = connection.prepareStatement("UPDATE voting SET active=? WHERE uuid=?");
			
			statement.setInt(1, 0);
			statement.setString(2, uuid.toString());
			
			statement.execute();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public HashMap<String, Integer> topVotes() {
		try {
			@Cleanup Connection connection = datasource.getConnection();
			@Cleanup PreparedStatement statement = connection.prepareStatement("SELECT COUNT(id) as amount, uuid FROM voting WHERE active=1 GROUP BY uuid ORDER BY amount");
			
			@Cleanup ResultSet rs = statement.executeQuery();
			
			HashMap<UUID, Integer> topvotes = new HashMap<>();
			
			while (rs.next()) {
				topvotes.put(UUID.fromString(rs.getString("uuid")), rs.getInt("amount"));
			}
			
			HashMap<String, Integer> convertedTopVotes = new HashMap<>();
			
			try {
				Map<UUID, String> convertedNames = new NameFetcher(new ArrayList<>(topvotes.keySet())).call();
				
				convertedNames.forEach((key, name) -> {
					convertedTopVotes.put(name, topvotes.get(key));
				});				
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
			return convertedTopVotes;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Vote> getVotesOfToday(UUID uuid) {
		try {
			@Cleanup Connection connection = datasource.getConnection();
			@Cleanup PreparedStatement statement = connection.prepareStatement("SELECT * FROM voting where uuid=? AND DATE(`when_voted`) = CURDATE()");
			
			statement.setString(1, uuid.toString());
			
			@Cleanup ResultSet rs = statement.executeQuery();
			
			ArrayList<Vote> votes = new ArrayList<>();
			
			while (rs.next()) {
				votes.add(new Vote(uuid, rs.getString("servicename"), rs.getTimestamp("when_voted")));
			}
			
			return votes;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
}
