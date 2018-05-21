package persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import records.UserInfo;
import records.UserStatistics;

public class SQLUserInfoPersistence implements UserInfoPersistence {

	Map<String, UserInfo> userInfoMap;
	private Connection conn;
	public SQLUserInfoPersistence(Connection conn) {
		this.conn = conn;
		userInfoMap = new HashMap<>();
	}

	@Override
	public void createUser(UserInfo user) {
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO UserInfo (username, password) VALUES (?, ?)");
				stmt.setString(1, user.getUsername());
				stmt.setString(2, user.getPassword());
				stmt.execute();
				userInfoMap.put(user.getUsername(), user);
				System.out.println("Record created");
			} catch (SQLException | IllegalArgumentException e) {
				System.err.println("Error inserting record: "+e);
			}
	}

	@Override
	public UserInfo searchUser(String user) {
		System.out.println("searching for user " + user);
		if (userInfoMap.containsKey(user))
		{
			return userInfoMap.get(user);
		}
		System.out.println("User not found: " + user);
		return null;
	}

	@Override
	public void generateUserList() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT username, password, totalGames, totalWins, timePlayed, `rank` FROM UserInfo");
			while(rs.next()) {
				String username = rs.getString(1);
				String password = rs.getString(2);
				Integer totalGames = rs.getInt(3);
				Integer totalWins = rs.getInt(4);
				Integer timePlayed = rs.getInt(5);
				Integer rank = rs.getInt(6);
//				System.out.println("Birthday of "+rs.getString(1)+" is on "+rs.getString(2));
				UserStatistics us = new UserStatistics(username, totalGames, totalWins, timePlayed);
				us.setRank(rank);
				UserInfo user = new UserInfo(username, password, us);
				userInfoMap.put(username, user);
			}
		} catch (SQLException e) {
			System.err.println("Error listing records: "+e);
			e.printStackTrace();
		}
	}

	@Override
	public List<UserStatistics> getUserStatsList() {
		// TODO Auto-generated method stub
		List<UserStatistics> statsList = new ArrayList<>();
		this.userInfoMap.forEach((string, userObj)->{
			if (userObj.getUserStats() != null)
			{
				userObj.getUserStats().computeAvgTime();
				statsList.add(userObj.getUserStats());
			}
		});
		statsList.sort(UserStatistics.AvgTimeComparator);
		int r = 1;
		for (UserStatistics us : statsList)
		{
			us.setRank(r);
			r++;
		}
		return statsList;
	}

	@Override
	public void updateUser(UserInfo user) throws IOException {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE UserInfo SET totalGames = ?, totalWins = ?, timePlayed = ? WHERE username = ?");
			stmt.setInt(1, user.getUserStats().getNumberOfGames());
			stmt.setInt(2, user.getUserStats().getNumberOfWins());
			stmt.setFloat(3, user.getUserStats().getTotalTimePlayed());
			stmt.setString(4, user.getUsername());
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.println("Information of "+user.getUsername()+" updated");
			} else {
				System.out.println(user.getUsername()+" not found!");
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}

	@Override
	public void recomputeRankings() {
		List<UserStatistics> stats = this.getUserStatsList();
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE UserInfo SET `rank` = ? WHERE username = ?");
			stats.forEach(i-> {
				try {
					stmt.setInt(1, i.getRank());
					stmt.setString(2, i.getUsername());
					int rows = stmt.executeUpdate();
					if(rows > 0) {
						System.out.println("Information of "+i.getUsername()+" updated");
					} else {
						System.out.println(i.getUsername()+" not found!");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.generateUserList();
		}
	}

}
