package persistence;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import records.OnlineUser;
import records.UserInfo;

public class TOnlineUserPersistence implements OnlineUserPersistence{

	private String fileName;
	private Path file;
	private static final Charset ENCODING = Charset.forName("UTF-8");
	private Map<String, OnlineUser> onlineUserMap;
	
	public TOnlineUserPersistence(String filename)
	{
		fileName = filename;
		file = Paths.get(fileName);
		onlineUserMap = new HashMap<>();
//		System.out.println("OP Ini"  + file.toAbsolutePath().toString());
	}


	@Override
	public void logoutUser(UserInfo user) throws IOException {
		// TODO Auto-generated method stub
		if (onlineUserMap.containsKey(user.getUsername()))
		{
			List<String> onlineUsersList, newOnlineUsersList;
			onlineUsersList = Files.readAllLines(file, ENCODING);
			newOnlineUsersList = onlineUsersList
				.stream()
				.map(c -> {
					System.out.println(c + " " + c.startsWith(user.getUsername()));
					return c;
				})
				.filter((c) -> !c.startsWith(user.getUsername()))
				.collect(Collectors.toList());
			onlineUserMap.remove(user.getUsername());
			System.out.println(onlineUserMap.size() + newOnlineUsersList.toString());
			Files.write(file, newOnlineUsersList, ENCODING, StandardOpenOption.TRUNCATE_EXISTING);
		}
		else 
		{
			throw new IOException("Unable to logout");
		}
	}


	@Override
	public void clearFile() throws IOException {
		// TODO Auto-generated method stub
		Files.deleteIfExists(file);
		Files.createFile(file);
	}


	@Override
	public boolean loginUser(UserInfo user) throws IOException {
		if (!onlineUserMap.containsKey(user.getUsername())){
			System.out.println("Added user to login map");
			OnlineUser newOnlineUser = new OnlineUser(user.getUsername());
			onlineUserMap.put(user.getUsername(), newOnlineUser);
			List<String> userOnline = Collections.singletonList(newOnlineUser.getUsername() + " " + newOnlineUser.getLoginTime().toString());
			Files.write(file, userOnline, ENCODING, StandardOpenOption.APPEND);
			return true;
		}
		return false;
	}


	@Override
	public boolean isLoggedIn(UserInfo user) {
		return onlineUserMap.containsKey(user.getUsername());
	}
	
	
	
}
