package persistence;

import java.io.IOException;
import java.util.List;

import records.UserInfo;
import records.UserStatistics;

public interface UserInfoPersistence {

	public void createUser(UserInfo user);
	public UserInfo searchUser(String user);
	public void generateUserList();
	public List<UserStatistics> getUserStatsList();
	public void updateUser(UserInfo user) throws IOException;
	public void recomputeRankings();
}
