package Model.User;

import Model.Exceptions.NotSuchPrivilegeException;
import Model.Exceptions.UnauthorizedException;
import Model.Relation.Relation;
import Model.Relation.RelationManager;
import Model.Security.AuthenticationManager;
import Model.Security.TrippleDes;
import Model.Security.Verification;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.Properties;


public class UserManager {
    private TrippleDes td;
    private final static Logger logger = Logger.getLogger(UserManager.class);//
    private AuthenticationManager authManager;

    public UserManager(){
        try{
            td=new TrippleDes();
        }
        catch (Exception e){

        }
        authManager=new AuthenticationManager();
    }

    /*
    Login
    code 0 = logged
    code 1 = not logged
     */
    public int login(User user){
        int code=1;
        boolean a;
        String e,c;
        e=user.getEmail();
        c=user.getContrasena();
        String encriptedpass=td.encrypt(c);
        a=user.login(e,encriptedpass);
        if(a) {
            user.selectUserByMail(user.getEmail());
            code=0;
        }
        else{user=null;}

        return code;
    }

    /*
    Funcion registrarse
    code 0 = registrado
    code 1 = usuario ya utilizado
    code 2 = bad request
     */
    public int register(User user){
        boolean a=false,b=true;
        int code;
            a = checkNullFields(user);
            if (!a) {
                b = user.checkUserExistent(user.getEmail());
                if (!b) {
                    user.setAdmin(0);
                    String encriptedpass = td.encrypt(user.getContrasena());
                    user.setContrasena(encriptedpass);
                    user.insertUser();
                    user.selectUserByMail(user.getEmail());
                    code = 0;
                } else {
                    code = 1;
                }
            } else {
                code = 2;
            }
        return code;
    }

    /*
    Funcion registrarse
    code 0 = registrado
    code 1 = usuario ya utilizado
    code 2 = bad request
     */
    public int addUser(HttpHeaders header, User user)throws UnauthorizedException, NotSuchPrivilegeException{
        boolean a=false,b=true;
        int code=2;
        Verification v = new Verification();
        try {
            authManager.verify(header,v);
            authManager.verifyAdmin(v);
            a = checkNullFields(user);
            if (!a) {
                b = user.checkUserExistent(user.getEmail());
                if (!b) {
                    String encriptedpass = td.encrypt(user.getContrasena());
                    user.setContrasena(encriptedpass);
                    user.insertUser();
                    user.selectUserByMail(user.getEmail());
                    code = 0;
                } else {
                    code = 1;
                }
            } else {
                code = 2;
            }
        }catch (UnauthorizedException ex) {
            throw new UnauthorizedException("Unauthorized: user is not authorized");

        }catch (NotSuchPrivilegeException ex){
            throw new NotSuchPrivilegeException("Forbidden: User has not privileges");

        }catch(Exception e){
            logger.info("INFO: error al modificar usuario");
            a=false;
        }

        return code;
    }

    /*
    Modificar usuario
    true = correcto
    false = no modificado
     */

    //falta arreglar si no se quieren cambiar todos los campos
    public boolean updateUser(HttpHeaders header, int id, User user) throws UnauthorizedException, NotSuchPrivilegeException{
        Boolean a=false;
        String encriptedpass;
        Verification v = new Verification();
        try {
            authManager.verify(header,v);
            authManager.verifyCorrectUser(v, id);
            user.setId(id);
            checkUpdateFields(user);
            a = user.updatetUser();
        }catch (UnauthorizedException ex) {
            throw new UnauthorizedException("Unauthorized: user is not authorized");

        }catch (NotSuchPrivilegeException ex){
            throw new NotSuchPrivilegeException("Forbidden: User has not privileges");

        }catch(Exception e){
            logger.info("INFO: error al modificar usuario");
            a=false;
        }
        return a;
    }

    public boolean changeAdmin(HttpHeaders header, int id, User user)throws UnauthorizedException, NotSuchPrivilegeException{
        Boolean a=false;
        String encriptedpass;
        Verification v = new Verification();
        try {
            authManager.verify(header,v);
            authManager.verifyAdmin(v);
            user.setId(id);
            user.changeAdmin(user.getAdmin());
            a=true;
            //a = user.updatetUser();
        }catch (UnauthorizedException ex) {
            throw new UnauthorizedException("Unauthorized: user is not authorized");

        }catch (NotSuchPrivilegeException ex){
            throw new NotSuchPrivilegeException("Forbidden: User has not privileges");

        }catch(Exception e){
            logger.info("INFO: error al modificar usuario");
            a=false;
        }
        return a;
    }

    public User getUserById(HttpHeaders header, int id)throws UnauthorizedException, NotSuchPrivilegeException{
        Verification v = new Verification();
        User u= new User();

        try {
            authManager.verify(header, v);
            authManager.verifyCorrectUser(v, id);
            u.selectUserById(id);

        }catch (UnauthorizedException ex) {
            throw new UnauthorizedException("Unauthorized: user is not authorized");

        }catch (NotSuchPrivilegeException ex){
            throw new NotSuchPrivilegeException("Forbidden: User has not privileges");
        }
        return u;
    }

    public User getUserByEmail(String email){
        User u= new User();
        u.selectUserByMail(email);
        return u;
    }


    public void deleteUser(HttpHeaders header, int id) throws UnauthorizedException, NotSuchPrivilegeException{
        Verification v = new Verification();
        RelationManager rm = new RelationManager();

        try {
            authManager.verify(header, v);
            authManager.verifyAdmin(v);
            rm.deleteRelationByUser(id, header);
            User u = new User();
            u.selectUserById(id);
            u.deleteUser();
        }catch (UnauthorizedException ex) {
            throw new UnauthorizedException("Unauthorized: user is not authorized");

        }catch (NotSuchPrivilegeException ex){
            throw new NotSuchPrivilegeException("Forbidden: User has not privileges");
        }
    }

    public List listAllUsers(HttpHeaders header) throws UnauthorizedException, NotSuchPrivilegeException{
        Verification v = new Verification();
        List<User> list;

        try {
            authManager.verify(header, v);
            authManager.verifyAdmin(v);
            list = new User().findAllUsers();
        }catch (UnauthorizedException ex) {
            throw new UnauthorizedException("Unauthorized: user is not authorized");

        }catch (NotSuchPrivilegeException ex){
            throw new NotSuchPrivilegeException("Forbidden: User has not privileges");
        }

        return list;
    }

    public boolean resetPassword(User u) {
        boolean bool;
        u.selectUserByMail(u.getEmail());
        final String username = u.getEmail();
        final String password = td.decrypt(u.getContrasena());

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(u.getEmail()));
            message.setSubject("Recuperar contraseña");
            message.setText("Hola, " + u.getNombre()
                    + "\n\n Tu contraseña es: " + password);

            Transport transport = session.getTransport("smtp");

            // Enter your correct gmail UserID and Password
            // if you have 2FA enabled then provide App Specific Password

            transport.connect("smtp.gmail.com", "DSAproyecto@gmail.com", "aleixguillemmikel");
            message.setFrom(new InternetAddress("DSAproyecto@gmail.com"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Done");
            bool = true;

        } catch (MessagingException e) {
            bool = false;
            throw new RuntimeException(e);
        }
        return bool;

    }

    public String generateToken(User u){
        String token = new AuthenticationManager().getToken(u);
        return token;
    }

    /*private methods*/

    private boolean checkNullFields(User u){
        if(u.getNombre().equals("")||u.getContrasena().equals("")||u.getEmail().equals(""))
            return true;

        return false;
    }

    private void checkUpdateFields(User u){
        User temp = new User();
        temp.selectUserById(u.getId());

        u.setAdmin(temp.getAdmin());

        if(u.getNombre()==null || u.getNombre().equals("")){
            u.setNombre(temp.getNombre());
        }
        if(u.getContrasena()==null || u.getContrasena().equals("")){
            u.setContrasena(temp.getContrasena());
        }else{
            String encryptedPass=td.encrypt(u.getContrasena());
            u.setContrasena(encryptedPass);
        }
        if (u.getEmail()==null || u.getEmail().equals("")){
            u.setEmail(temp.getEmail());
        }

    }

}
