//
//  HuaQiStoreWebViewController.swift
//  PointExchange
//
//  Created by yiner on 2018/7/21.
//  Copyright © 2018年 WannaWin. All rights reserved.
//

import UIKit
import WebKit

class HuaQiStoreWebViewController: UIViewController,WKNavigationDelegate {

	
	@IBOutlet weak var webView: WKWebView!
	var backBtn: UIBarButtonItem!
	var forwardBtn: UIBarButtonItem!
	var refreshBtn: UIBarButtonItem!
	
	override func viewDidLoad() {
		super.viewDidLoad()
		backBtn = UIBarButtonItem(title: "后退", style: .plain, target: self, action: #selector(HuaQiStoreWebViewController.onButtonsClicked(_:)))
		backBtn.tag = 1
		
		forwardBtn = UIBarButtonItem(title: "前进", style: .plain, target: self, action: #selector(HuaQiStoreWebViewController.onButtonsClicked(_:)))
		forwardBtn.tag = 2
		
		refreshBtn = UIBarButtonItem(title: "刷新", style: .plain, target: self, action: #selector(HuaQiStoreWebViewController.onButtonsClicked(_:)))
		refreshBtn.tag = 3
		
		self.navigationItem.leftBarButtonItems = [backBtn,forwardBtn]
		self.navigationItem.rightBarButtonItem = refreshBtn
		
	}
	
	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		self.refreshButtonState()
		let url = NSURL(string: "https://www.citibank.com.cn/sim/ICARD/cardoffer.htm")
		let request = NSURLRequest(url: url! as URL)
		self.webView.load(request as URLRequest)
		self.webView.navigationDelegate = self
	}
	
	func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
		self.refreshButtonState()
	}

	
	func refreshButtonState() {
		if self.webView.canGoBack {
			self.backBtn.isEnabled = true
		}
		else {
			self.backBtn.isEnabled = false
		}
		
		if self.webView.canGoForward {
			self.forwardBtn.isEnabled = true
		}
		else {
			self.forwardBtn.isEnabled = false
		}
		
	}

	
	@objc func onButtonsClicked(_ sender:UIBarButtonItem) {
		switch (sender.tag) {
			case 1:
				self.webView.goBack()
				self.refreshButtonState()
			case 2:
				self.webView.goForward()
				self.refreshButtonState()
			default:
				self.webView.reload()
		}
	}
	
	
	override func didReceiveMemoryWarning() {
		super.didReceiveMemoryWarning()
		// Dispose of any resources that can be recreated.
	}


}
