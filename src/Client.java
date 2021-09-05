import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InetAddress ip;
		try {
			ip = InetAddress.getByName("localhost");
			Socket clientSocket= new Socket(ip,5109);
			Scanner inpuut=new Scanner(System.in);
			DataInputStream input=new DataInputStream(clientSocket.getInputStream());
			DataOutputStream output=new DataOutputStream(clientSocket.getOutputStream());
			while(true)
			{
				String conn= input.readUTF();
				System.out.println("Server :"+conn);
				
				String request=inpuut.nextLine();
				output.writeUTF(request);
				
				if(request.equalsIgnoreCase("Quit"))
				{		
					clientSocket.close();
					System.out.print("Connection is closed ");
					System.out.println();
					
				}
				else if(request.equalsIgnoreCase("Login"))
				{
					String usernameReq=input.readUTF();
					System.out.println("Server :"+usernameReq);
					
					String usernameResponse=inpuut.nextLine();
					output.writeUTF(usernameResponse);
					
					String validateUsername=input.readUTF();
					System.out.println("Server :"+validateUsername);
					
					if(validateUsername.contains("wrong"))
						continue;
					
					String passwordResponse=inpuut.nextLine();
					output.writeUTF(passwordResponse);
					
					String validatePassword=input.readUTF();
					System.out.println("Server :"+validatePassword);
					
					if(validatePassword.contains("wrong"))
						continue;
					
					while(true)
					{
						String commandRequest=input.readUTF();
						System.out.println("Server :"+commandRequest);
						
						String commandResponse=inpuut.nextLine();
						output.writeUTF(commandResponse);
						
						if(commandResponse.equalsIgnoreCase("QUIT"))
						{		
							clientSocket.close();
							System.out.print("Connection is closed ");
							System.out.println();
							break;
						}
						
						else if(commandResponse.equalsIgnoreCase("SHOW DIRECTORIES"))
						{
							String Response=input.readUTF();
							System.out.println("Server :"+Response);
						}
						else if(commandResponse.equalsIgnoreCase("SHOW DIRECTORY"))
						{
							String DirResponse=input.readUTF();
							System.out.println("Server :"+DirResponse);
							
							String FolderName=inpuut.nextLine();
							output.writeUTF(FolderName);
							
							String FolderResponse=input.readUTF();
							System.out.println("Server :"+FolderResponse);	
							
						}
						else if (commandResponse.equalsIgnoreCase("DOWNLOAD FILE"))
						{
							String downloadResponse=input.readUTF();
							System.out.println("Server :"+downloadResponse);
							
							String downloadFileName=inpuut.nextLine();
							output.writeUTF(downloadFileName);
							
							if(downloadFileName.contains(","))
							{
								String [] FileList=downloadFileName.split(",");
								for(int i=0;i<FileList.length;i++)
								{
									String containtResponse=input.readUTF();
									System.out.println("Server :"+containtResponse);
									String DownloadDir=input.readUTF();
									Download(DownloadDir,containtResponse);
								}
								
							}
							else
							{
								String containtResponse=input.readUTF();
								System.out.println("Server :"+containtResponse);
								String DownloadDir=input.readUTF();
								Download(DownloadDir,containtResponse);
								
								
							}			
							
						}
						else if(commandResponse.equalsIgnoreCase("Upload File"))
						{
							String UploadResponse=input.readUTF();
							System.out.println("Server :"+UploadResponse);
							
							String uploadFileName=inpuut.nextLine();
							output.writeUTF(uploadFileName);
							
							String UploadCheck=input.readUTF();
							System.out.println("Server :"+UploadCheck);
						}
						else if(commandResponse.equalsIgnoreCase("Upload File to another clinet"))
						{
							String UploadResponse=input.readUTF();
							System.out.println("Server :"+UploadResponse);
							
							String uploadFileName=inpuut.nextLine();
							output.writeUTF(uploadFileName);
							
							String uploadUserName=input.readUTF();
							System.out.println("Server :"+uploadUserName);
							
							String userNameReply=inpuut.nextLine();
							output.writeUTF(userNameReply);
							
							String UploadCheck=input.readUTF();
							System.out.println("Server :"+UploadCheck);
						}
						else
						{
							String Reply=input.readUTF();
							System.out.println("Server :"+Reply);
							
						}
						
					}
					
					
				}
				else
				{
					String Reply=input.readUTF();
					System.out.println("Server :"+Reply);
					
				}
			}
			
			
		}
		catch (UnknownHostException e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		catch (IOException e) 
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

	}

	}
