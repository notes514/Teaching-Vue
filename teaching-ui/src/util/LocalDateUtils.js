/**
 * 日期对象转年月日时分秒
 *
 * @param date 日期对象
 * @returns {string} 返回字符串格式的日期时间
 */
export function formatDate(date) {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    let hours = date.getHours();
    let mins = date.getMinutes();
    let secs = date.getSeconds();
    if (hours < 10) {
        hours = "0" + hours;
    }
    if (mins < 10) {
        mins = "0" + mins;
    }
    if (secs < 10) {
        secs = "0" + secs;
    }
    console.log(year + "-" + month + "-" + day + " " + hours + ":" + mins + ":" + secs)
    return year + "-" + month + "-" + day + " " + hours + ":" + mins + ":" + secs;
}

/**
 * 验证输入号码是否为11位有效手机号码
 *
 * @param phone 手机号码
 * @returns {boolean} 返回true表示正确，反之亦然
 */
export function isPoneAvailable(phone) {
    let isPhoneFlag = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
    return isPhoneFlag.test(phone.val());
}