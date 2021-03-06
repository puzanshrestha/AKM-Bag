USE [Bags]
GO
/****** Object:  User [NT AUTHORITY\SYSTEM]    Script Date: 4/19/2018 11:04:25 AM ******/
CREATE USER [NT AUTHORITY\SYSTEM] FOR LOGIN [NT AUTHORITY\SYSTEM] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [NT AUTHORITY\SYSTEM]
GO
/****** Object:  Table [dbo].[bagDetails]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[bagDetails](
	[bag_id] [int] IDENTITY(1,1) NOT NULL,
	[bag_name] [varchar](50) NULL,
	[bag_category] [varchar](50) NULL,
	[bag_price] [float] NULL,
	[bag_company] [varchar](50) NULL,
	[bag_photo] [varchar](50) NULL,
	[vendor_id] [int] NULL,
 CONSTRAINT [PK_bagDetails] PRIMARY KEY CLUSTERED 
(
	[bag_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[bagStock]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[bagStock](
	[stock_id] [int] IDENTITY(1,1) NOT NULL,
	[bag_color] [varchar](50) NULL,
	[bag_id] [int] NULL,
	[quantity_color] [int] NULL,
 CONSTRAINT [PK_bagStock] PRIMARY KEY CLUSTERED 
(
	[stock_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[customerDetails]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[customerDetails](
	[customer_id] [int] IDENTITY(1,1) NOT NULL,
	[customer_name] [varchar](50) NULL,
	[customer_address] [varchar](50) NULL,
	[customer_phone] [varchar](15) NULL,
 CONSTRAINT [PK__customer__CD65CB8580A2E5DB] PRIMARY KEY CLUSTERED 
(
	[customer_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[discount]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[discount](
	[d_id] [int] IDENTITY(1,1) NOT NULL,
	[d_percent] [int] NULL,
	[d_value] [int] NULL,
 CONSTRAINT [PK_dicount] PRIMARY KEY CLUSTERED 
(
	[d_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[mOrderDetails]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[mOrderDetails](
	[mOrderId] [int] IDENTITY(1,1) NOT NULL,
	[bag_id] [int] NULL,
	[bag_name] [varchar](15) NULL,
	[bag_color] [varchar](15) NULL,
	[bag_price] [int] NULL,
	[customerName] [varchar](25) NULL,
	[customer_id] [int] NULL,
	[quantity] [int] NULL,
	[date] [date] NULL,
	[discount] [int] NULL,
 CONSTRAINT [PK_mOrderDetails] PRIMARY KEY CLUSTERED 
(
	[mOrderId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[orderDetails]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[orderDetails](
	[orderId] [int] NULL,
	[bag_id] [int] NULL,
	[bag_name] [varchar](50) NULL,
	[bag_color] [varchar](50) NULL,
	[bag_price] [int] NULL,
	[quantity] [int] NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[orders]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[orders](
	[order_id] [int] IDENTITY(1,1) NOT NULL,
	[customer_name] [varchar](50) NULL,
	[customer_id] [int] NULL,
	[discount] [int] NULL,
	[date] [date] NULL,
	[shop_number] [int] NOT NULL,
	[receipt_no] [int] NULL,
 CONSTRAINT [PK_orders] PRIMARY KEY CLUSTERED 
(
	[order_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[pendingBill]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[pendingBill](
	[pId] [int] IDENTITY(1,1) NOT NULL,
	[date] [date] NULL,
	[customer_name] [varchar](50) NULL,
	[customer_id] [int] NULL,
	[total] [int] NULL,
	[address] [varchar](50) NULL,
	[shop_number] [int] NOT NULL,
 CONSTRAINT [PK_pendingBill] PRIMARY KEY CLUSTERED 
(
	[pId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[pendingBillDetails]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[pendingBillDetails](
	[p_details_id] [int] NOT NULL,
	[bag_id] [int] NULL,
	[bag_name] [varchar](50) NULL,
	[bag_price] [int] NULL,
	[bag_color] [varchar](50) NULL,
	[quantity] [int] NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[relation]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[relation](
	[rId] [int] IDENTITY(1,1) NOT NULL,
	[bag_id] [int] NULL,
	[vendor_id] [int] NULL,
 CONSTRAINT [PK_relation] PRIMARY KEY CLUSTERED 
(
	[rId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[users]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[users](
	[user_id] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](50) NULL,
	[password] [varchar](50) NULL,
	[userType] [int] NULL,
	[shop_number] [int] NOT NULL,
 CONSTRAINT [PK_users] PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[vendorDetails]    Script Date: 4/19/2018 11:04:25 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[vendorDetails](
	[vendor_id] [int] IDENTITY(1,1) NOT NULL,
	[vendor_name] [varchar](50) NULL,
	[vendor_address] [varchar](50) NULL,
 CONSTRAINT [PK__vendorDe__0F7D2B78DEB93AF0] PRIMARY KEY CLUSTERED 
(
	[vendor_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
ALTER TABLE [dbo].[orders] ADD  CONSTRAINT [DF_orders_shop_number]  DEFAULT ((1)) FOR [shop_number]
GO
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [DF_users_shop_number]  DEFAULT ((1)) FOR [shop_number]
GO
ALTER TABLE [dbo].[pendingBillDetails]  WITH CHECK ADD  CONSTRAINT [FK_pendingBillDetails_bagDetails] FOREIGN KEY([bag_id])
REFERENCES [dbo].[bagDetails] ([bag_id])
GO
ALTER TABLE [dbo].[pendingBillDetails] CHECK CONSTRAINT [FK_pendingBillDetails_bagDetails]
GO
