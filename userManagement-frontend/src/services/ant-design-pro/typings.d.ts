// @ts-ignore
/* eslint-disable */

declare namespace API {
  type BaseResponse<T> = {
    code?: number,
    message?:string,
    description?: string,
    data?:T
  }
  type CurrentUser = {
    id?: number;
    userAccount?: string;
    userName?: string;
    avatarUrl?: string;
    createTime?: string;
    updateTime?: string;
    email?: string;
    gender?: number;
    isDelete?: number;
    userRole?: number;
    userStatus?: number;
    phone?: string;
    planetCode?: string;
  };

  type LoginResult = {
    id?: number;
    userAccount?: string;
    userName?: string;
    avatarUrl?: string;
    createTime?: string;
    updateTime?: string;
    email?: string;
    gender?: number;
    isDelete?: number;
    userRole?: number;
    userStatus?: number;
    phone?: string;
    planetCode?: string;

    status?: string;
    type?: string;
    currentAuthority?: string;
  };

  type RegisterResult = number;

  type PageParams = {
    current?: number;
    pageSize?: number;
  };

  type RuleListItem = {
    key?: number;
    disabled?: boolean;
    href?: string;
    avatar?: string;
    name?: string;
    owner?: string;
    desc?: string;
    callNo?: number;
    status?: number;
    updatedAt?: string;
    createdAt?: string;
    progress?: number;
  };

  type RuleList = {
    data?: RuleListItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  };

  type FakeCaptcha = {
    code?: number;
    status?: string;
  };

  type LoginParams = {
    userAccount?: string;
    passWord?: string;
    autoLogin?: boolean;
    type?: string;
  };

  type RegisterParams = {
    userAccount?: string;
    passWord?: string;
    checkPassWord?: string;
    planetCode?: string;
    type?: string;
  };

  type ErrorResponse = {
    /** 业务约定的错误码 */
    errorCode: string;
    /** 业务上的错误信息 */
    errorMessage?: string;
    /** 业务上的请求是否成功 */
    success?: boolean;
  };

  type NoticeIconList = {
    data?: NoticeIconItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  };

  type NoticeIconItemType = 'notification' | 'message' | 'event';

  type NoticeIconItem = {
    id?: string;
    extra?: string;
    key?: string;
    read?: boolean;
    avatar?: string;
    title?: string;
    status?: string;
    datetime?: string;
    description?: string;
    type?: NoticeIconItemType;
  };
}
