//
//  UserViewController.swift
//  PointExchange
//
//  Created by Pan on 2018/7/5.
//  Copyright © 2018年 WannaWin. All rights reserved.
//

import UIKit

class UserViewController: UITableViewController {
    
    @IBOutlet weak var portraitImageView: UIImageView!
    @IBOutlet weak var exitButton: UITableViewCell!
    @IBOutlet weak var logoutButton: UIView!
    // 等待动画
    var activityIndicator:UIActivityIndicatorView?
    //登录后头部
    var boundingCitiCardHeadLabel:UILabel = UILabel()
    var usernameLabel:UILabel = UILabel()
    
    //未登录时登录按钮
    var loginButton:UIButton = UIButton()
    
    let storyBoard = UIStoryboard(name: "User", bundle: nil)
    
    @IBOutlet weak var userTableCell: UITableViewCell?

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tableView.reloadData()
		activityIndicator = ActivityIndicator.createWaitIndicator(parentView: self.tableView)
        
    }
    
    @objc func gotoLogin(){
        let storyBoard = UIStoryboard(name:"User", bundle:nil)
        let view = storyBoard.instantiateViewController(withIdentifier: "LoginViewController")
        self.navigationController!.pushViewController(view, animated: true)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath:IndexPath){
        if (indexPath as NSIndexPath).section == 2 && (indexPath as NSIndexPath).row == 0{
            let alert = UIAlertController(title:"退出登录", message:"是否确认退出登录？", preferredStyle:.alert)
            let okAction=UIAlertAction(title:"确定", style:.default, handler:{ action in
                self.logout()
            })
            let cancelAction=UIAlertAction(title:"取消", style:.cancel, handler:nil)
            
            alert.addAction(cancelAction)
            alert.addAction(okAction)
            self.present(alert, animated:true, completion:nil)
        }
        else if indexPath.section == 0 {
            if let _ = User.getUser().username {
                let storyboard = UIStoryboard(name:"User", bundle:nil)
                let view = storyboard.instantiateViewController(withIdentifier: "UserSettingViewController")
                self.navigationController!.pushViewController(view, animated: true)
            }
        }
        else if indexPath.section == 1 {
            
            switch indexPath.row{
            // 绑定花旗账户
            case 0:
                bindAccount()
            // 查看积分兑换记录
            case 1:
                checkPointRecord()
            // 查看我的订单
            case 2:
                checkHistory()
            default:
                break;
            }
            
        }
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            // 如果登录，显示用户名，若未登录，显示登录按钮
            if let name = User.getUser().username{
                usernameLabel.textColor=UIColor.darkGray
                usernameLabel.frame=CGRect(x:143, y:47, width:200, height:21)
                usernameLabel.text=name
                boundingCitiCardHeadLabel.text = {() -> String in
                    if let card = User.getUser().card{
                        return "银行卡："
                    }
                    else{
                        return "您尚未绑定银行卡"
                    }
                }()
                boundingCitiCardHeadLabel.textColor=UIColor.darkGray
                boundingCitiCardHeadLabel.frame=CGRect(x:143, y:70, width:200, height:21)
                
                userTableCell?.addSubview(usernameLabel)
                userTableCell?.addSubview(boundingCitiCardHeadLabel)
                
                userTableCell?.accessoryType = UITableViewCellAccessoryType.disclosureIndicator
                userTableCell?.isUserInteractionEnabled = true
                
                
                
                loginButton.removeFromSuperview()
            }
            else{
                loginButton.frame=CGRect(x:175, y:47, width:133, height:50)
                loginButton.setTitleColor(UIColor.white, for:.normal)
                loginButton.setTitle("登录 / 注册", for: UIControlState.normal)
                loginButton.layer.borderWidth=2
                loginButton.layer.borderColor=UIColor.white.cgColor
                loginButton.layer.cornerRadius=15
                loginButton.addTarget(self, action: #selector(UserViewController.gotoLogin), for: .touchDown)
                userTableCell?.addSubview(loginButton)
                userTableCell?.accessoryType = UITableViewCellAccessoryType.none
                userTableCell?.selectionStyle = .none
                
                
                boundingCitiCardHeadLabel.removeFromSuperview()
                usernameLabel.removeFromSuperview()
                
                
                
            }
            //头像显示
            self.portraitImageView.image = User.getUser().getPortraitImage().roundCornersToCircle()
            return userTableCell!
        }
        else if indexPath.section == 2 {
            if let _ = User.getUser().username{
                exitButton.isUserInteractionEnabled = true
                exitButton.backgroundColor = UIColor.white
                exitButton.textLabel?.textColor = UIColor.white
            }else{
                exitButton.isUserInteractionEnabled = false
                exitButton.backgroundColor = UIColor.lightGray
                exitButton.textLabel?.textColor = UIColor.white
            }
            return super.tableView(self.tableView, cellForRowAt: indexPath)
        }
        else{
            return super.tableView(self.tableView, cellForRowAt: indexPath)
        }
        
    }

    /// 退出登录
    func logout(){
        User.logout()
        print("logout")
        self.tableView.reloadData()
        
    }

    
    /// 绑定花旗账户
    func bindAccount(){
        if let _ = User.getUser().username{
			activityIndicator?.startAnimating()
			ServerConnector.bindCitiCard { (result, url) in
				if result {
					let view = self.storyBoard.instantiateViewController(withIdentifier:"AddBankCardViewController") as! AddBankCardViewController
					view.url = url
					self.navigationController?.pushViewController(view, animated: true)
				}
			}
			
        }else{
            let view = storyBoard.instantiateViewController(withIdentifier:"LoginViewController")
            self.navigationController?.pushViewController(view, animated: true)
        }
    }
    
    /// 查看积分兑换记录
    func checkPointRecord(){
        if let _ = User.getUser().username{
            ServerConnector.getAllPointsHistory{ (result, pointsHistory) in
                if result {
                    let view = self.storyBoard.instantiateViewController(withIdentifier:"PointHistoryViewController") as! PointHistoryViewController
                    view.pointsHistoryArray = pointsHistory
                    self.navigationController?.pushViewController(view, animated: true)
                    
                }
                
            }
            
        }else{
            let view = storyBoard.instantiateViewController(withIdentifier:"LoginViewController")
            self.navigationController?.pushViewController(view, animated: true)
        }
    }
    
    /// 查看历史订单
    func checkHistory(){
        if let _ = User.getUser().username{
            activityIndicator?.startAnimating()
            let view = self.storyBoard.instantiateViewController(withIdentifier:"OrdersTableViewController") as! OrdersTableViewController
			self.navigationController?.pushViewController(view, animated: true)
			
            
            
        }else{
            let view = storyBoard.instantiateViewController(withIdentifier:"LoginViewController")
            self.navigationController?.pushViewController(view, animated: true)
        }
    }
}
