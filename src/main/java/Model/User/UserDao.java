package Model.User;

import Model.DAO.DAO;

import java.sql.*;
import java.util.List;

public class UserDao extends DAO {

    protected void insertUser(){
        insert();
    }
    protected boolean updatetUser(){
        return update();
    }
    protected void selectUserById(int id){
        select(id);
    }
    protected void deleteUser(){delete();}
    protected List findAllUsers(){
        return findAll();
    }
    protected boolean checkUserExistent(String email){
        return checkExistent("email",email);
    }

    protected boolean login(String email, String password) {
        boolean logeado = false;
        Connection con = getConnection();
        StringBuffer query = new StringBuffer("SELECT nombre,contrasena FROM ");
        query.append(this.getClass().getSimpleName());
        query.append(" WHERE email='" + email + "' AND contrasena='" + password + "';");
        try {
            PreparedStatement ps = con.prepareStatement(query.toString());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                logger.info("INFO: No logeado: " + email);
                logeado = false;

            } else {
                logger.info("INFO: Logeado: " + email);
                logeado = true;
            }
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logeado;
    }

    //buscar por email en la base de datos
    protected void selectUserByMail(String email) {
        Connection con = getConnection();
        StringBuffer query = new StringBuffer("SELECT * FROM ");
        query.append(this.getClass().getSimpleName());
        query.append(" WHERE email='");
        query.append(email);
        query.append("';");

        try {
            PreparedStatement ps = con.prepareStatement(query.toString());
            logger.info("INFO: Select by email  statement: " + ps.toString());
            ;
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                setClassFields(rs, rsmd, this);
            }
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void changeAdmin(int id, int admin){
        Connection con = getConnection();
        StringBuffer query = new StringBuffer("UPDATE ");
        query.append(this.getClass().getSimpleName());
        query.append(" SET Admin=");
        query.append(admin);

        query.deleteCharAt(query.length() - 1);
        query.append(" WHERE id=");
        query.append(id);
        query.append(";");

        try {
            PreparedStatement ps = con.prepareStatement(query.toString());
            ps.executeUpdate();
            logger.info("INFO: Change admin statement: "+ps.toString());
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
