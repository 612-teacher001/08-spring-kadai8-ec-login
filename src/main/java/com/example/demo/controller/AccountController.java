package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Customer;
import com.example.demo.model.Account;
import com.example.demo.repository.CustomerRepository;


@Controller
public class AccountController {

	@Autowired
	HttpSession session;

	@Autowired
	Account account;
	
	@Autowired
	CustomerRepository customerRepository;

	// ログイン画面を表示
	@GetMapping({ "/login", "/logout" })
	public String index(
			@RequestParam(name = "error", defaultValue = "") String error,
			Model model) {
		// セッション情報を全てクリアする
		session.invalidate();
		// エラーパラメータのチェック
		if (error.equals("notLoggedIn")) {
			model.addAttribute("message", "ログインしてください");
		}

		return "login";
	}

	// ログインを実行
	@PostMapping("/login")
	public String login(
			@RequestParam("name") String name,
			Model model) {
		// 名前が空の場合にエラーとする
		if (name == null || name.length() == 0) {
			model.addAttribute("message", "名前を入力してください");
			return "login";
		}

		// セッション管理されたアカウント情報に名前をセット
		account.setName(name);

		// 「/items」へのリダイレクト
		return "redirect:/items";
	}
	
	// 会員登録画面表示
	@GetMapping("/account")
	public String create() {
		// 画面遷移
		return "accountForm";
	}
	
	// 会員登録処理
	@PostMapping("/account")
	public String store(@RequestParam(defaultValue = "") String name,
						@RequestParam(defaultValue = "") String address,
						@RequestParam(defaultValue = "") String phone,
						@RequestParam(defaultValue = "") String email,
						@RequestParam(defaultValue = "") String password) {
		// リクエストパラメータをもとにして顧客クラスをインスタンス化
		Customer customer = new Customer(name, address, phone, email, password);
		// 顧客インスタンスをcustomersテーブルに登録
		customerRepository.save(customer);
		// 画面遷移
		return "redirect:/login";
	}
	
	
}
