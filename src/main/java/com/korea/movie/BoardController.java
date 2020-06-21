package com.korea.movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.Common;
import common.Paging2;
import dao.BoardDAO;
import vo.BoardVO;
import vo.UserVO;

@Controller
public class BoardController {

	@Autowired
	HttpServletRequest request;
	HttpServletResponse response;

	BoardDAO board_dao;

	public void setBoard_dao(BoardDAO board_dao) {
		this.board_dao = board_dao;
	}
	
	//로그인 확인하는 매핑
	@RequestMapping("/checkLogin.do")
	@ResponseBody
	public String checkLogin(String id, String m_name) {//String id
		HttpSession session = request.getSession();
		UserVO user = (UserVO)session.getAttribute("user");
		String content = board_dao.selectReview(id, m_name);

		String res = "";
		if(user == null) {
			res = "no";
		}else {
			if(content == null ) {
				res= user.getId();
			}else {
				res = "already";
			}
		}
		return res;
	}

	//리뷰 작성하는 폼으로 이동
	@RequestMapping("/insert_form.do")
	public String insert_form() {
		return Common.Board.VIEW_PATH + "review_insert.jsp";
	}

	//수정하기 위한 폼으로 이동
	@RequestMapping("/modify_form.do")
	public String modify_form(Model model, String id, String m_name, String type, String totalVar1, String totalVar2) {//나중에 바인딩 할거라 model 필요

		BoardVO vo = board_dao.selectModify(id , m_name);
		model.addAttribute("vo", vo);
		model.addAttribute("type", type);
		model.addAttribute("totalVar1", totalVar1);
		model.addAttribute("totalVar2", totalVar2);
		return Common.Board.VIEW_PATH + "review_modify_form.jsp";    
	}

	//리뷰 등록
	@RequestMapping("/insert.do")
	public String insert(BoardVO vo, String type, String totalVar1, String totalVar2) {

		String content = vo.getContent().replaceAll("<br>", "\n");
		vo.setContent(content);
		board_dao.insert(vo);

		if( type.equals("1") ) { 
			return "redirect:movieInfoDetail.do&movieId="+totalVar1+"&movieSeq="+totalVar2+"&m_name="+vo.getM_name();
		} else {
			return "redirect:movieInfoDetailRank.do&title="+totalVar1+"&releaseDts="+totalVar2;
		}
	}

	//수정 
	@RequestMapping("/modify.do")
	public String modify(BoardVO vo, String type, String totalVar1, String totalVar2) {
		String content = vo.getContent().replaceAll("<br>", "\n");
		vo.setContent(content);

		board_dao.update(vo);
		if( type.equals("1") ) {  
			return "redirect:movieInfoDetail.do&movieId="+totalVar1+"&movieSeq="+totalVar2+"&m_name="+vo.getM_name();
		} else if (type.equals("2")){
			return "redirect:movieInfoDetailRank.do&title="+totalVar1+"&releaseDts="+totalVar2;
		}else {
			return "redirect:review.do";
		}
	}

	//삭제
	@RequestMapping("/delete.do")
	@ResponseBody
	public String delete(String id, String m_name) {
		System.out.println("삭제할때 데이터들 제목:"+m_name + "/아이디 : " + id);
		int result = board_dao.delete(id, m_name);
		String res = "no";
		if(result != 0) {
			res = "yes";
		}
		return res;
	}

	@RequestMapping("/review.do")
	public String reviews(Model model, Integer page) {

		//한 페이지에 표시되는 게시물의 시작과 끝 번호를 계산
		int nowPage = 1;
		if(page != null) {
			nowPage = page; 
		}
		
		int start = (nowPage - 1) * Common.Board.BLOCKLIST2 + 1;
		int end = start + Common.Board.BLOCKLIST2 - 1;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);

		List<BoardVO>list = null;
		list = board_dao.selectListTotal(map);

		String content = "";
		for(int i = 0; i < list.size(); i++) {
			content = list.get(i).getContent().replaceAll("\n", "<br>");
			list.get(i).setContent(content);
		}

		//전체 게시물 수 구하기
		int row_total = board_dao.getRowTotal2();
		
		String pageMenu = Paging2.getPaging("review.do", nowPage, row_total, Common.Board.BLOCKLIST2, Common.Board.BLOCKPAGE);

		model.addAttribute("list", list);
		model.addAttribute("pageMenu", pageMenu); 
		model.addAttribute("count", row_total);

		return Common.Board.VIEW_PATH + "community.jsp";   
	}

	@RequestMapping("/selectreview.do")
	@ResponseBody
	public List<BoardVO> mypageReview(String id){
		List<BoardVO> list = null;
		list=board_dao.myReview(id);
		return list;
	}

}
