package com.korea.movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.Common;
import common.Paging;
import dao.MovieListDAO;
import dao.BoardDAO;
import vo.BoardVO;
import vo.MovieRankPosterVO;
import vo.MovieRecordVO;

@Controller
public class MovieListController {

	MovieListDAO movieListDAO;
	BoardDAO board_dao;

	public void setMovieListDAO(MovieListDAO movieListDAO) {
		this.movieListDAO = movieListDAO;
	}
	public void setBoard_dao(BoardDAO board_dao) {
		this.board_dao = board_dao;
	}

	@Autowired
	HttpServletRequest request;

	//영화 리스트 출력
	@RequestMapping ("/movieReleaseList.do" )
	public String movieReleaseList() {
		return Common.Movie.VIEW_PATH + "movie_list.jsp";
	}

	//박스오피스 api의 포스터와 예고편이 없는 것을 DB로 보완
	@RequestMapping("/moviePosterLoad.do")
	@ResponseBody
	public List<MovieRankPosterVO> loadRankPoster() {
		List<MovieRankPosterVO> list = null;
		list = movieListDAO.selectList();
		return list;
	}

	//영화를 검색하면 DB에 기록
	@RequestMapping("/movieQueryRecord.do")
	@ResponseBody
	public int insert(MovieRecordVO vo) {
		int res = 0;

		String ip = request.getRemoteAddr();
		vo.setIp(ip);
		res = movieListDAO.insert(vo);
		return res;
	}
	
	//type2 : 현재상영작에서 영화상세페이지로 넘아갈때의 필요한 파라미터를 가진 매핑
	@RequestMapping("/movieInfoDetailBoxOffice.do")
	public String goMovieInfoDetail2(Model model, Integer page, String releaseDts, String title, String trailer) {
		int type = 2;
		int nowPage = 1;
		if(page != null) {
			nowPage = page;
		}

		//한 페이지에 표시되는 게시물의 시작과 끝 번호를 계산
		int start = (nowPage - 1) * Common.Board.BLOCKLIST + 1;
		int end = start + Common.Board.BLOCKLIST - 1;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("m_name", title);

		List<BoardVO>list = null;
		list = board_dao.selectList(map);

		String content = "";
		for(int i = 0; i < list.size(); i++) {
			content = list.get(i).getContent().replaceAll("\n", "<br>");
			list.get(i).setContent(content);
		}

		//전체 게시물 수 구하기
		int row_total = board_dao.getRowTotal(title);

		//Paging클래스를  사용하여 페이지 메뉴 생성하기                                                                              
		String pageMenu = Paging.getPaging("movieInfoDetailBoxOffice.do?title="+title+"&releaseDts="+releaseDts, nowPage, row_total, Common.Board.BLOCKLIST, Common.Board.BLOCKPAGE);
		int bunmo = board_dao.selectNum(title);
		if(bunmo == 0) {
			bunmo = 1;
		}

		float avg_f = (float)board_dao.selectSum(title) / bunmo;
		float avg_f2 = Float.parseFloat(String.format("%.1f", avg_f));
		int avg = board_dao.selectSum(title) / bunmo;

		// String user_m_name = board_dao.selectM(title);

		model.addAttribute("list", list);
		model.addAttribute("avg_f2", avg_f2);
		model.addAttribute("avg", avg);

		// model.addAttribute("user_m_name", user_m_name);
		model.addAttribute("pageMenu", pageMenu);
		model.addAttribute("type", type);
		model.addAttribute("releaseDts", releaseDts);
		model.addAttribute("title", title);
		model.addAttribute("trailer", trailer);
		model.addAttribute("count", row_total);

		return Common.Movie.VIEW_PATH + "movie_detail.jsp";
	}

	//영화별 전체 리뷰보기
	//type1 : 상영예정작에서 영화상세페이지로 넘아갈때의 필요한 파라미터를 가진 매핑
	@RequestMapping("/movieInfoDetail.do")
	public String list(Model model, Integer page, String movieId, String movieSeq, String m_name) {
		int type = 1;
		int nowPage = 1;
		if(page != null) {
			nowPage = page;
		}

		//한 페이지에 표시되는 게시물의 시작과 끝 번호를 계산
		int start = (nowPage - 1) * Common.Board.BLOCKLIST + 1;
		int end = start + Common.Board.BLOCKLIST - 1;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("m_name", m_name);

		List<BoardVO>list = null;
		list = board_dao.selectList(map); 

		String content = "";
		for(int i = 0; i < list.size(); i++) {
			content = list.get(i).getContent().replaceAll("\n", "<br>");
			list.get(i).setContent(content);
		}

		//전체 게시물 수 구하기
		int row_total = board_dao.getRowTotal(m_name); 

		//Paging클래스를  사용하여 페이지 메뉴 생성하기
		String pageMenu = Paging.getPaging("movieInfoDetail.do?movieId="+movieId +"&movieSeq="+movieSeq +"&m_name="+m_name, nowPage, row_total, Common.Board.BLOCKLIST, Common.Board.BLOCKPAGE);

		int bunmo = board_dao.selectNum(m_name);
		if(bunmo == 0) {
			bunmo = 1;
		}
		int avg = board_dao.selectSum(m_name) / bunmo;

		float avg_f = (float)board_dao.selectSum(m_name) / bunmo;
		float avg_f2 = Float.parseFloat(String.format("%.1f", avg_f));

		model.addAttribute("list", list);
		model.addAttribute("avg_f2", avg_f2);
		model.addAttribute("avg", avg);
		model.addAttribute("pageMenu", pageMenu);
		model.addAttribute("type", type);
		model.addAttribute("m_name", m_name);
		model.addAttribute("movieId", movieId);
		model.addAttribute("movieSeq", movieSeq);
		model.addAttribute("count", row_total);

		return Common.Movie.VIEW_PATH + "movie_detail.jsp";
	}
	

}
