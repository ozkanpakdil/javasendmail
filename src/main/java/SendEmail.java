import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

@SuppressWarnings("static-access")
public class SendEmail {
    static Options options = new Options();
    static Option to = new Option("to", "to-mail", true, "use given email for to");
    static Option from = new Option("from", "from-mail", true, "use given email for from");
    static Option smtp = new Option("smtp", "smtp-host", true, "SMTP host");
    static Option subject = new Option("s", "subject", true, "mail subject");
    static Option body = new Option("b", "body-message", true, "mail body");

    static {
	to.setRequired(true);
	smtp.setRequired(true);
	options.addOption(to);
	options.addOption(from);
	options.addOption(smtp);
	options.addOption(subject);
	options.addOption(body);
    }

    public static void main(String[] args) {
	CommandLineParser parser = new GnuParser();

	CommandLine cmd = null;
	try {
	    cmd = parser.parse(options, args);
	} catch (ParseException e) {
	    printCliHelp(e.getMessage());
	}

	String mailto = cmd.getOptionValue(to.getOpt(), "root@localhost");
	String fromMail = cmd.getOptionValue(from.getOpt(), "root@localhost");
	String host = cmd.getOptionValue(smtp.getOpt(), "localhost");
	String subjectTxt = "This is the Subject Line!";
	String text = "This is actual message";
	if (cmd.hasOption(subject.getOpt()))
	    subjectTxt = cmd.getOptionValue(subject.getOpt());
	if (cmd.hasOption(body.getOpt()))
	    text = cmd.getOptionValue(body.getOpt());
	Properties properties = System.getProperties();
	properties.setProperty("mail.smtp.host", host);

	Session session = Session.getDefaultInstance(properties);
	try {
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(fromMail));
	    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailto));
	    message.setSubject(subjectTxt);
	    message.setText(text);
	    Transport.send(message);
	    System.out.println("Sent message successfully....");
	} catch (MessagingException mex) {
	    System.out.println("Sent message problem:");
	    mex.printStackTrace();
	}
    }

    private static void printCliHelp(String message) {
	System.out.println(message);
	HelpFormatter formatter = new HelpFormatter();
	formatter.printHelp("java -jar " + SendEmail.class.getName() + ".jar", options);
	System.out.println("example:");
	System.out.println("java -jar SendEmail.jar -b test -from who@com.com -smtp localhost -s title -to hello@body.com");
	System.exit(-1);
    }

}
