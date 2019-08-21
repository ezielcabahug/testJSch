import com.jcraft.jsch.*;

import java.io.*;

public class TestJSchClass {
    public static void main(String[] args) {
        String username = "remote_username";
        String hostname = "remote_hostname";
        String password = "remote_password";
        String command = "sudo remote_command.sh";

        JSch jsch = new JSch();
        Session session;
        Channel channel;
        InputStream inputStream;
        OutputStream outputStream;
        BufferedReader bufferedReader;
        String line;

        try {
            session = jsch.getSession(username, hostname);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            outputStream = channel.getOutputStream();
            ((ChannelExec) channel).setErrStream(System.err);
            inputStream = channel.getInputStream();
            ((ChannelExec) channel).setPty(true);
            channel.connect();
            outputStream.write((password + "\n").getBytes());
            outputStream.flush();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = bufferedReader.readLine()) != null) {
                System.out.println("Server Said: ");
                System.out.println(line);
            }

            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
