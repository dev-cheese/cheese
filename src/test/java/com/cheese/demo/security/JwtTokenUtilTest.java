package com.cheese.demo.security;

import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberRoleEnum;
import com.cheese.demo.mock.DeviceDummy;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenUtilTest {

    private static final String TEST_USER_EMAIL = "test@gmail.com";

    @Mock
    private Clock clockMock;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 3600L); // one hour
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "mySecret");
    }

    @Test
    public void testGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() {
        when(clockMock.now())
                .thenReturn(DateUtil.yesterday())
                .thenReturn(DateUtil.now());

        final String token = createToken();
        final String laterToken = createToken();

        assertThat(token).isNotEqualTo(laterToken);
    }

    @Test
    public void getUsernameFromToken() {
        when(clockMock.now()).thenReturn(DateUtil.now());

        final String token = createToken();
        final String usernameFromToken = jwtTokenUtil.getUsernameFromToken(token);

        assertThat(usernameFromToken).isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    public void getCreatedDateFromToken() {
        final Date now = DateUtil.now();
        when(clockMock.now()).thenReturn(now);

        final String token = createToken();
        final Date issuedAtDateFromToken = jwtTokenUtil.getIssuedAtDateFromToken(token);

        assertThat(issuedAtDateFromToken).isInSameMinuteWindowAs(now);
    }

    @Test
    public void getExpirationDateFromToken() {
        final Date now = DateUtil.now();
        when(clockMock.now()).thenReturn(now);
        final String token = createToken();

        final Date expirationDateFromToken = jwtTokenUtil.getExpirationDateFromToken(token);
        assertThat(DateUtil.timeDifference(expirationDateFromToken, now)).isCloseTo(3600000L, within(1000L));
    }

    @Test
    public void getAudienceFromToken() {
        when(clockMock.now()).thenReturn(DateUtil.now());
        final String token = createToken();
        final String audienceFromToken = jwtTokenUtil.getAudienceFromToken(token);

        assertThat(audienceFromToken).isEqualTo(JwtTokenUtil.AUDIENCE_WEB);
    }

    @Test(expected = ExpiredJwtException.class)
    public void expiredTokenCannotBeRefreshed() {
        when(clockMock.now())
                .thenReturn(DateUtil.yesterday());
        String token = createToken();

        jwtTokenUtil.canTokenBeRefreshed(token, DateUtil.tomorrow());
    }

    @Test
    public void changedPasswordCannotBeRefreshed() {
        when(clockMock.now())
                .thenReturn(DateUtil.now());
        String token = createToken();

        assertThat(jwtTokenUtil.canTokenBeRefreshed(token, DateUtil.tomorrow())).isFalse();
    }

    @Test
    public void notExpiredCanBeRefreshed() {
        when(clockMock.now())
                .thenReturn(DateUtil.now());
        String token = createToken();
        assertThat(jwtTokenUtil.canTokenBeRefreshed(token, DateUtil.yesterday())).isTrue();
    }

    @Test
    public void canRefreshToken() {
        when(clockMock.now())
                .thenReturn(DateUtil.now())
                .thenReturn(DateUtil.tomorrow());
        String firstToken = createToken();
        String refreshedToken = jwtTokenUtil.refreshToken(firstToken);
        Date firstTokenDate = jwtTokenUtil.getIssuedAtDateFromToken(firstToken);
        Date refreshedTokenDate = jwtTokenUtil.getIssuedAtDateFromToken(refreshedToken);
        assertThat(firstTokenDate).isBefore(refreshedTokenDate);
    }

    @Test
    public void canValidateToken() {
        when(clockMock.now())
                .thenReturn(DateUtil.now());
        UserDetails userDetails = mock(JwtUser.class);

        when(userDetails.getUsername()).thenReturn(TEST_USER_EMAIL);
        String token = createToken();
        assertThat(jwtTokenUtil.validateToken(token, userDetails)).isTrue();
    }


    private String createToken() {
        final DeviceDummy device = new DeviceDummy();
        device.setNormal(true);

        final Member member = Member.builder()
                .email(TEST_USER_EMAIL)
                .role(MemberRoleEnum.ADMIN)
                .build();

        final JwtUser jwtUser = JwtUserFactory.buildJwtUser(member);

        return jwtTokenUtil.generateToken(jwtUser, device);
    }

}