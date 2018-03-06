package persistence;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import records.UserInfo;
import records.UserStatistics;

public class TUserInfoPersistence implements UserInfoPersistence {

	Map<String, UserInfo> userInfoMap;
	private String fileName;
	private Path file;
	private static final Charset ENCODING = Charset.forName("UTF-8");
	public TUserInfoPersistence(String fName) {
		userInfoMap = new HashMap<>();
		fileName = fName;
		file = Paths.get(fileName);
	}

	public void generateUserList() {
		List<String> userInfoList;
		try {			
			userInfoList = Files.readAllLines(file, ENCODING);
			userInfoList.forEach(user -> {
				String[] info = user.split(" +");
				if (info.length == 2)
				{
					UserInfo userObj = new UserInfo(info[0], info[1]);
					userInfoMap.putIfAbsent(info[0], userObj);
				}
			});
			System.out.println("Found: " + userInfoMap.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void createUser(UserInfo user) {
		UserInfo test = searchUser(user.getUsername());
		System.out.println("Registering User Now...");
		if (test == null)
		{
			List<String> userInfo = Collections.singletonList(user.getUsername() + " " + user.getPassword());
			try {
				Files.write(file, userInfo, ENCODING, StandardOpenOption.APPEND);
				userInfoMap.put(user.getUsername(), user);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public UserInfo searchUser(String user) {
		System.out.println("searching user " + user);
		if (userInfoMap.containsKey(user))
		{
			return userInfoMap.get(user);

		}
		return null;
	}

	@Override
	public List<UserStatistics> getUserStatsList() {
		List<UserStatistics> statsList = new ArrayList<>();
		this.userInfoMap.forEach((string, userObj)->{
			userObj.getUserStats().computeAvgTime();
			statsList.add(userObj.getUserStats());
		});
		return statsList;
	}

}
