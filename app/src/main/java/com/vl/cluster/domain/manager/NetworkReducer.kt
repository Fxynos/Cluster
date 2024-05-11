package com.vl.cluster.domain.manager

import com.vl.cluster.domain.exception.ConnectionException
import com.vl.cluster.domain.boundary.Network
import com.vl.cluster.domain.boundary.Session
import com.vl.cluster.domain.exception.CaptchaException
import com.vl.cluster.domain.exception.TwoFaException
import com.vl.cluster.domain.exception.UnsupportedLoginMethodException
import com.vl.cluster.domain.exception.WrongCredentialsException
import com.vl.cluster.domain.boundary.NetworkAuth
import java.util.stream.Stream

class NetworkReducer(val networks: List<Network>) {


}