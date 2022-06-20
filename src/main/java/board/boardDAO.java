package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class boardDAO {
	private Connection conn; 
	private ResultSet rs; 
	public boardDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/yhj";
			String dbID = "root";
			String dbPassword = "1234";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getDate() {
		String sql = "select now()";
		try {
			PreparedStatement pstmt; 
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public int getNext() {
		String sql = "select postID from board order by postID desc";
		try {
			PreparedStatement pstmt; 
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫 번째 게시물인 경우
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	public int write(String postTitle, String userID, String postContent) {
		String sql = "insert into board values(?, ?, ?, ?, ?, ?);";
		try {
		
			PreparedStatement pstmt; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, postTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, postContent);
			pstmt.setInt(6, 1); // 글의 고유 번호
			pstmt.executeUpdate();
			System.out.println("11111");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스오류
	}
	
	public ArrayList<boardDTO> getList(int pageNumber){
		String sql = "select * from board where postID < ? and postAvailable = 1 order by postID desc limit 10";
		
		System.out.println("1234");
		ArrayList<boardDTO> list = new ArrayList<boardDTO>();
		try {
			PreparedStatement pstmt; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				boardDTO dto = new boardDTO();
				dto.setPostID(rs.getInt(1));
				dto.setPostTitle(rs.getString(2));
				dto.setUserID(rs.getString(3));
				dto.setPostDate(rs.getString(4));
				dto.setPostContent(rs.getString(5));
				dto.setPostAvailable(rs.getInt(6));
				list.add(dto);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public boolean nextPage(int pageNumber) {
		String sql = "select * from board where postID < ? and postAvailable = 1";
		try {
			PreparedStatement pstmt; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boardDTO getBoard(int postID) {
		String sql = "select *from board where postID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, postID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				boardDTO dto = new boardDTO();
				dto.setPostID(rs.getInt(1));
				dto.setPostTitle(rs.getString(2));
				dto.setUserID(rs.getString(3));
				dto.setPostDate(rs.getString(4));
				dto.setPostContent(rs.getString(5));
				dto.setPostAvailable(rs.getInt(6));
				return dto;
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int postID, String postTitle, String postContent) {
		String sql = "update board set postTitle = ?, postContent = ? where postID = ?";

		try {
			PreparedStatement pstmt; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, postTitle);
			pstmt.setString(2, postContent);
			pstmt.setInt(3, postID);

			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스오류
	}
	
	public int delete(int postID){
		String sql = "update board set postAvailable = 0 where postID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, postID);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}


