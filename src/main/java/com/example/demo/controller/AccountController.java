package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	// 会員登録画面表示
	@GetMapping("/account")
	public String create(Model model) {
		// スコープに登録
		model.addAttribute("customer", new Customer());
		// 画面遷移
		return "accountForm";
	}
	
	// 会員登録処理
	@PostMapping("/account")
	public String store(@RequestParam(defaultValue = "") String name,
						@RequestParam(defaultValue = "") String address,
						@RequestParam(defaultValue = "") String phone,
						@RequestParam(defaultValue = "") String email,
						@RequestParam(defaultValue = "") String password,
						Model model) {
		// 入力値チェック
		List<String> errorList = new ArrayList<String>();
		if (name.isEmpty()) {
			// 名前必須入力エラー
			errorList.add("名前は必須です");
		}
		if (address.isEmpty()) {
			// 住所必須入力エラー
			errorList.add("住所は必須です");
		}
		if (phone.isEmpty()) {
			// 電話番号必須入力エラー
			errorList.add("電話番号は必須です");
		}
		if (email.isEmpty()) {
			// メールアドレス必須入力エラー
			errorList.add("メールアドレスは必須です");
		} else {
			// Optionalの説明：
			Optional<Customer> exsists = customerRepository.findByEmail(email);
			if (exsists.isPresent()) {
				errorList.add("登録済みのメールアドレスです");
			}
		}
		if (password.isEmpty()) {
			// パスワード必須入力エラー
			errorList.add("パスワードは必須です");
		}
		// リクエストパラメータをもとにして顧客クラスをインスタンス化
		Customer customer = new Customer(name, address, phone, email, password);
		
		// エラーがある場合
		if (errorList.size() > 0) {
			model.addAttribute("customer", customer);
			model.addAttribute("errors", errorList);
			return "accountForm";
		}
		
		// 顧客インスタンスをcustomersテーブルに登録
		customerRepository.save(customer);
		// 画面遷移
		return "redirect:/login";
	}
	
	// ログイン処理
	@PostMapping("/login")
	public String login(@RequestParam String email,
						@RequestParam String password,
						Model model) {
		// リクエストパラメータをもとにくcustomersテーブルから顧客クラスを取得
		Optional<Customer> customerOpt = customerRepository.findByEmailAndPassword(email, password);
		
		if (customerOpt.isPresent()) {
			// 顧客インスタンスを取得できた場合 ⇒ ユーザ認証に成功した場合：アカウントインスタンスに名前を設定
			Customer customer = customerOpt.get();
			account.setName(customer.getName());
		} else {
			model.addAttribute("error", "メールアドレスとパスワードが一致しませんでした");
			return "login";
		}
		
		// 画面遷移
		return "redirect:/items";
	}
	
	
}
