import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java. util. Arrays;
import java.util.Collections;
public class session extends Thread {
	static int UserCount=4;
	static String workingDir = System.getProperty("user.dir");
	public static String[][] readCredentials()
	{
		String Credentials [][]=new String [UserCount][2];
	
		try {
			
			FileInputStream file=new FileInputStream(workingDir+"\\src\\Credentials.txt");
			Scanner input=new Scanner(file);
			int row=0;
			while(input.hasNextLine())
			{
				String [] Line=input.nextLine().split(":");
				
				Credentials[row][0]=Line[0].trim();
				Credentials[row][1]=Line[1].trim();
				row+=1;
	
			}
	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Credentials;
		
	}
	static int index=-1;
	public static void Validation(String Credentials [][],String username)
	{
		for(int i=0;i<UserCount;i++)
		{	
			
			if(Credentials[i][0].equals(username))
			{
				index=i;
				break;
			}
			else 
			{
				index=-1;
			}
			
		}
		
	}
	public static String [] userDir()
	{
		String Directions []=new String [UserCount];
	
		try {
			
			FileInputStream file=new FileInputStream(workingDir+"\\src\\Directories.txt");
			Scanner input=new Scanner(file);
			int row=0;
			while(input.hasNextLine())
			{
				String [] Line=input.nextLine().split(",");
				Directions[row]=Line[1].trim();
				row+=1;
			}	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Directions;
	}
	public static String[] showDir(String Dir) {
        File dir = new File(Dir);
        String contents[] = dir.list();
        return contents;
    }
	public static String showContaint(String Dir) throws IOException
	{
		FileReader fileReader;
		String Containt="";
		try {
			fileReader = new FileReader (Dir);
			BufferedReader in = new BufferedReader(fileReader);
			String line;

			while ((line = in.readLine())!= null) 
			{
				Containt+=line+"\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Containt;
	}
	public static void Download(String fileName,String Contain)
	{
		
		try {
			BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName));
			buffer.write(Contain);
			buffer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	Socket clientSocket;
	public session(Socket clientSocket) {
		this.clientSocket=clientSocket;
	}
	@Override
	public void run() {
		Scanner inpuut = new Scanner(System.in);
		try {
		
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream output=new DataOutputStream(clientSocket.getOutputStream());
			while(true)
			{
				output.writeUTF("Please choose ‘LOGIN’ or ‘QUIT’");
				String request=input.readUTF();
				if(request.equalsIgnoreCase("Quit"))
	        	{
					clientSocket.close();
					System.out.println("Conection with this client is closed");
					System.out.println();
					break;
	        	}
				else if (request.equalsIgnoreCase("Login"))
				{
					output.writeUTF("Please enter your username");
					String usernameResponse=input.readUTF();
					String Arr[][]=readCredentials();
					Validation(Arr,usernameResponse.trim());
					if(index != -1)
					{
						output.writeUTF("Username OK, Please enter your password");
						
						String passwordResponse=input.readUTF();
						if(Arr[index][1].equals(passwordResponse))
						{
							output.writeUTF("Login Successfully");
							
							String finalFileDir="";
							while(true)
							{
								
								output.writeUTF("Please choose: ‘SHOW DIRECTORIES’ or ‘SHOW DIRECTORY’ or ‘DOWNLOAD FILE’ or ‘Upload File’ or ‘Upload File to another clinet’ or ‘QUIT’ ");
								String commandResponse=input.readUTF();
								
								String userDirections [] = userDir();
								String curruntUserDirections=workingDir+userDirections[index];
								
								String uploadDir = workingDir+"\\src\\Uploads\\"+Arr[index][0];
								
								String otherDir=workingDir+"\\src";
										
								if(commandResponse.equalsIgnoreCase("QUIT"))
					        	{
									clientSocket.close();
									System.out.println("Conection with this client is closed");
									return;
					        	}
								else if(commandResponse.equalsIgnoreCase("SHOW DIRECTORIES"))
								{
									String FoldersArr[] = showDir(curruntUserDirections);
									String Folders="";
									for(int i=0;i<FoldersArr.length;i++)
									{
										Folders+=FoldersArr[i];
										if(i==FoldersArr.length-1)
											break;
										Folders+=",";
									}
									output.writeUTF("The available directories are: "+ Folders);
								
								}
								else if (commandResponse.equalsIgnoreCase("SHOW DIRECTORY"))
								{
									output.writeUTF("Please enter the desired directory");
									
									String DircName=input.readUTF(); // Movies
									String finalDir = curruntUserDirections+"\\"+DircName;
									finalFileDir=finalDir;
									
									String FileArr[] = showDir(finalDir);
									String Files="";
									for(int i=0;i<FileArr.length;i++)
									{
										Files+=FileArr[i];
										if(i==FileArr.length-1)
											break;
										Files+=",";
									}
									output.writeUTF("The available files are: "+ Files);
								}
								else if(commandResponse.equalsIgnoreCase("DOWNLOAD FILE"))
								{
									output.writeUTF("Please choose the desired file to download");
									
									String downloadFileName=input.readUTF();
									
									if(downloadFileName.contains(","))
									{
										String [] FileList=downloadFileName.split(",");
										String temp = finalFileDir;
										for(int i=0;i<FileList.length;i++)
										{
											temp = finalFileDir+"\\"+FileList[i]+".txt";
											
											String Containt = showContaint(temp);
											output.writeUTF(Containt);
											
											
											String DownloadDir = curruntUserDirections+"\\Download\\"+FileList[i]+".txt";
											output.writeUTF(DownloadDir);
											
											
											
										}
										
									}
									else
									{
										
										String temp = finalFileDir+"\\"+downloadFileName+".txt";
										
										String Containt = showContaint(temp);
										output.writeUTF(Containt);
										
										
										String DownloadDir = curruntUserDirections+"\\Download\\"+downloadFileName+".txt";
										output.writeUTF(DownloadDir);
										
										//Download(DownloadDir,Containt);
								
									}

									
								}
								else if(commandResponse.equalsIgnoreCase("Upload File"))
								{
									output.writeUTF("Please choose the desired file to Upload");
									
									String uploadFileName=input.readUTF();
									
									String temp = finalFileDir+"\\"+uploadFileName+".txt";
									
									String Containt = showContaint(temp);
									
									String Upload = uploadDir+"\\"+uploadFileName+".txt";
									Download(Upload,Containt);
									
									output.writeUTF("File Uploaded");
										
								}
								else if(commandResponse.equalsIgnoreCase("Upload File to another clinet"))
								{
									output.writeUTF("Please choose the desired file to Upload");
									String uploadFileName=input.readUTF();
									
									output.writeUTF("Please enter the username of reciver to send");
									
									String userNameReply=input.readUTF();
									
									String temp = finalFileDir+"\\"+uploadFileName+".txt";
									String Containt = showContaint(temp);
									
									String curuntAntherUpload=otherDir+"\\"+userNameReply+"\\Others\\"+uploadFileName+".txt";
								
									Download(curuntAntherUpload,Containt);
									
									output.writeUTF("File Sent to "+userNameReply);
									
									
								}
								else
								{
									output.writeUTF("Please enter a valid command ");
									continue;
								}
							}
							
							
						}
						else
						{
							
							output.writeUTF("Password is wrong please enter a valid Password again ");
							continue;
						}
						
					}
					else
					{
							output.writeUTF("Username is wrong please enter a valid username again ");
							continue;
					}
							
				}
				else
				{
					output.writeUTF("Please enter a valid command ");
					continue;
				}
				
			}
			
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
