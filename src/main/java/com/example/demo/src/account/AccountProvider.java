package com.example.demo.src.account;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.account.model.GetAccountRes;
import com.example.demo.src.follow.FollowDao;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.oauth.AuthDao;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import com.example.demo.src.point.PointDao;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.PostLoginRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.example.demo.utils.JwtService;
import org.springframework.transaction.annotation.Transactional;
import static com.example.demo.config.BaseResponseStatus.*;


@Service
public class AccountProvider {
    private final AccountDao accountDao;
    private final JwtService jwtService;

    @Autowired
    public AccountProvider(AccountDao accountDao , JwtService jwtService)
    {
        this.accountDao = accountDao;
        this.jwtService = jwtService;

    }

    public int checkUser(int user_id) throws BaseException
    {
        try{
            return accountDao.checkUserStatus(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void checkDefault(int user_id) throws BaseException
    {
        try{
             accountDao.checkDefault(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetAccountRes> getAccounts(int user_id) throws BaseException
    {
        try{
            return accountDao.getAccounts(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
