package urjc.isi.dao.implementaciones;

import java.sql.*;
import java.sql.Statement;
import java.sql.PreparedStatement;

import urjc.isi.entidades.Peliculas;
import urjc.isi.dao.interfaces.PeliculasDAO;

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;

//Aqui vienen las cosas propias de cada tabla,
//es decir, las queries o cosas que no se
//puedan implementar de manera genérica

//A estos metodos son a los que llamaremos para
//implementar las distintas respuestas para el
//servidor
public class PeliculasDAOImpl extends GenericDAOImpl<Peliculas> implements PeliculasDAO{
  //Para meterlo como parte de interfaz hay que encontrar comodefinir que detecte
  // Peliculas como un objeto cualquiera, es decir que obligue a rellenar eso con
  //, por ejemplo, algo que extienda de Entidades
  @Override
  public void insert(Connection c, Peliculas entity) {
  	String sql = "INSERT INTO peliculas(idpelicula,titulo,año,duracion,rating,nvotos) VALUES(?,?,?,?,?,?)";

  	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
  		pstmt.setInt(1, entity.getIdPelicula());
  		pstmt.setString(2, entity.getTitulo());
      pstmt.setInt(3, entity.getAño());
      pstmt.setDouble(4, entity.getDuracion());
      pstmt.setDouble(5, entity.getRating());
      pstmt.setInt(6, entity.getNVotos());
  		pstmt.executeUpdate();
    } catch (SQLException e) {
  	    System.out.println(e.getMessage());
  	}
  }
  
  @Override
  public void createTable() throws SQLException{
		Statement statement = c.createStatement();
		statement.setQueryTimeout(30);
		statement.executeUpdate("drop table if exists peliculas");
		statement.executeUpdate("create table peliculas (id_pelicula INT, nombre string, fecha string, duracion string, rating INT, PRIMARY KEY (id_pelicula))");
  }

  @Override
  public void uploadTable(BufferedReader br, Connection c) throws IOException, SQLException {
    String s;
    while ((s = br.readLine()) != null) {
      Peliculas pelicula = new Peliculas(s);
      insert(c, pelicula);
      c.commit();
    }
  }

  @Override
  public void dropTable(Connection c) throws SQLException {
	  Statement statement = c.createStatement();
	  statement.setQueryTimeout(30);
	  statement.executeUpdate("drop table if exists peliculas");
  }

  @Override
  public Boolean tableExists(Connection c) throws SQLException {
	  DatabaseMetaData dbm = c.getMetaData();
	  ResultSet tables = dbm.getTables(null, null, "peliculas", null);
	  if (tables.next()) {
		  // La tabla existe
		  return true;
	  } else {
		  // No existe la tabla
		  return false;
	  }
  }
  @Override
  public List<Peliculas> selectAll(){
	  List<Peliculas> filmList = new ArrayList<>();
	  String sql = "SELECT * from peliculas";
	  try (PreparedStatement pstmt = c.prepareStatement(sql)) {
		  ResultSet rs = pstmt.executeQuery();
		  c.commit();
		  while(rs.next()){
			  Peliculas peli = new Peliculas();
			  peli.setIdPelicula(Integer.parseInt(rs.getString("idpelicula")));
			  peli.setTitulo(rs.getString("titulo"));
			  peli.setAño(Integer.parseInt(rs.getString("año")));
			  peli.setDuracion(Double.parseDouble(rs.getString("duracion")));
			  peli.setRating(Double.parseDouble(rs.getString("rating")));
			  peli.setNVotos(Integer.parseInt(rs.getString("nvotos")));
			  // Añado la peli a la lista de pelis
			  filmList.add(peli);
		  }
    } catch (SQLException e) {
		  System.out.println(e.getMessage());
	  }
	  return filmList;
  }
  @Override
  public Peliculas selectByID (Connection c, String idpelicula){
	  String sql = "SELECT * from peliculas WHERE idpelicula=" + idpelicula;
	  Peliculas peli = new Peliculas();
	  try (PreparedStatement pstmt = c.prepareStatement(sql)) {
		  ResultSet rs = pstmt.executeQuery();
		  c.commit();
		  while(rs.next()){
			  peli.setIdPelicula(Integer.parseInt(rs.getString("idpelicula")));
			  peli.setTitulo(rs.getString("titulo"));
			  peli.setAño(Integer.parseInt(rs.getString("año")));
			  peli.setDuracion(Double.parseDouble(rs.getString("duracion")));
			  peli.setRating(Double.parseDouble(rs.getString("rating")));
			  peli.setNVotos(Integer.parseInt(rs.getString("nvotos")));
		  }
      } catch (SQLException e) {
		  System.out.println(e.getMessage());
	  }
	  return peli;
  }
}
