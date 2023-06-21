import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable, TableDropdown} from '@ant-design/pro-components';
import {Button, Image, message} from 'antd';
import {useRef} from 'react';
import {delUserById, queryAllUser, updateUserById} from "@/services/ant-design-pro/api";

export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};
const columns: ProColumns<API.CurrentUser>[] = [
    {
      dataIndex: 'id',
      valueType: 'indexBorder',
      width: 48,
    },
    {
      title: '用户名',
      dataIndex: 'userName',
      copyable: true,
    },
    {
      title: '用户账户',
      dataIndex: 'userAccount',
      copyable: true,
    },
    {
      title: '头像',
      dataIndex: 'avatarUrl',
      search: false,
      render: (_, record) => (
        <div>
          <Image
            src={record.avatarUrl} width={100}
          />
        </div>
      ),
    },
    {
      title: '性别',
      dataIndex: 'gender',
      valueEnum: {
        0: {text: '男', status: 'Default'},
        1: {
          text: '女',
          status: 'Success',
        },
      },
    },
    {
      title: '电话',
      dataIndex: 'phone',
      copyable: true,
    },
    {
      title: '邮件',
      dataIndex: 'email',
      copyable: true,
    },
    {
      title: '状态',
      dataIndex: 'userStatus',
      valueEnum: {
        0: {text: '正常', status: 'Success'},
      },
    },
    {
      title: '星球编号',
      dataIndex: 'planetCode',
    },
    {
      title: '角色',
      dataIndex: 'userRole',
      valueType: 'select',
      valueEnum: {
        0: {text: '普通用户', status: 'Default'},
        1: {
          text: '管理员',
          status: 'Success',
        },
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      search: false
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            action?.startEditable?.(record.id);
          }}
        >
          编辑
        </a>,
        <TableDropdown
          key="actionGroup"
          onSelect={(key) => {
            if ('copy' === key){
              message.success('数据被复制')
            }
            action?.reload()
          }}
          menus={[
            {key: 'copy', name: '复制'},
          ]}
        />,
      ],
    },
  ]
;
export default () => {
  const actionRef = useRef<ActionType>();
  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      request={async (params: API.CurrentUser & {
        pageSize: number;
        current: number;
      }, sort, filter) => {
        console.log(sort, filter);
        const resp = await queryAllUser(params);
        return {
          data: resp.data
        }
      }}

      editable={{
        type: 'multiple',
        onSave: async (key, row): Promise<any | void> => {
          try {
            const resp = await updateUserById(row);
            if (resp && resp.code === 20000){
              message.success('更新成功');
              return;
            }
            throw new Error(`${resp.message},${resp.description}`)
          }catch (error: any){
            message.error(error.message);
          }
        },
        onDelete: async (key, row): Promise<any | void> => {
          try {
            const resp = await delUserById(row);
            if (resp && resp.code === 20000){
              message.success('删除成功');
              return;
            }
            throw new Error(`${resp.message},${resp.description}`)
          }catch (error: any){
            message.error(error.message);
          }
        },

      }}
      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
        onChange(value) {
          console.log('value: ', value);
        },
      }}
      rowKey="id"
      search={{
        labelWidth: 'auto',
      }}
      options={{
        setting: {
          listsHeight: 400,
        },
      }}
      form={{
        // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
        syncToUrl: (values, type) => {
          if (type === 'get') {
            return {
              ...values,
              created_at: [values.startTime, values.endTime],
            };
          }
          return values;
        },
      }}
      pagination={{
        pageSize: 5,
        onChange: (page) => console.log(page),
      }}
      dateFormatter="string"
      headerTitle="用户数据"
      toolBarRender={() => [
        <Button
          key="button"
          icon={<PlusOutlined/>}
          onClick={() => {
            //actionRef.current?.reload();
            message.error('弹框')
          }}
          type="primary"
        >
          新建
        </Button>,
      ]}/>
  );
};
