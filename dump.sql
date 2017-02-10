{\rtf1\ansi\ansicpg1252\cocoartf1504
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;\csgray\c100000;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx566\tx1133\tx1700\tx2267\tx2834\tx3401\tx3968\tx4535\tx5102\tx5669\tx6236\tx6803\pardirnatural\partightenfactor0

\f0\fs24 \cf0 -- phpMyAdmin SQL Dump\
-- version 4.4.10\
-- http://www.phpmyadmin.net\
--\
-- Host: localhost:8889\
-- Creato il: Gen 13, 2017 alle 14:09\
-- Versione del server: 5.5.42\
-- Versione PHP: 5.6.10\
\
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";\
SET time_zone = "+00:00";\
\
--\
-- Database: `plannerpath`\
--\
\
-- --------------------------------------------------------\
\
--\
-- Struttura della tabella `albero`\
--\
\
CREATE TABLE `albero` (\
  `id` int(11) NOT NULL,\
  `nome` varchar(255) DEFAULT NULL,\
  `splitsize` int(15) DEFAULT NULL,\
  `depth` int(15) DEFAULT NULL\
) ENGINE=InnoDB DEFAULT CHARSET=latin1;\
\
-- --------------------------------------------------------\
\
--\
-- Struttura della tabella `arco`\
--\
\
CREATE TABLE `arco` (\
  `edgeUid` int(11) NOT NULL,\
  `valore` int(15) NOT NULL,\
  `id_albero` int(15) NOT NULL\
) ENGINE=InnoDB DEFAULT CHARSET=latin1;\
\
-- --------------------------------------------------------\
\
--\
-- Struttura della tabella `AttrDef`\
--\
\
CREATE TABLE `AttrDef` (\
  `attrDefUid` int(11) NOT NULL,\
  `name` varchar(255) NOT NULL,\
  `nomeAlbero` varchar(255) NOT NULL\
) ENGINE=InnoDB DEFAULT CHARSET=latin1;\
\
-- --------------------------------------------------------\
\
--\
-- Struttura della tabella `EdgeAttrUsage`\
--\
\
CREATE TABLE `EdgeAttrUsage` (\
  `edgeAttrUid` int(11) NOT NULL,\
  `objectEdgeUid` int(11) NOT NULL,\
  `attrDefUid` int(11) NOT NULL,\
  `value` int(11) NOT NULL\
) ENGINE=InnoDB DEFAULT CHARSET=latin1;\
\
-- --------------------------------------------------------\
\
--\
-- Struttura della tabella `VertexAttrUsage`\
--\
\
CREATE TABLE `VertexAttrUsage` (\
  `vertexUsageUid` int(11) NOT NULL,\
  `objectVid` int(11) NOT NULL,\
  `attrDefId` int(11) NOT NULL,\
  `value` int(11) NOT NULL\
) ENGINE=InnoDB DEFAULT CHARSET=latin1;\
\
-- --------------------------------------------------------\
\
--\
-- Struttura della tabella `vertice`\
--\
\
CREATE TABLE `vertice` (\
  `vertexUid` int(11) NOT NULL,\
  `id_albero` int(15) DEFAULT NULL,\
  `nome` varchar(255) DEFAULT NULL,\
  `arco_entrante` int(15) DEFAULT NULL\
) ENGINE=InnoDB DEFAULT CHARSET=latin1;\
\
--\
-- Indici per le tabelle scaricate\
--\
\
--\
-- Indici per le tabelle `albero`\
--\
ALTER TABLE `albero`\
  ADD PRIMARY KEY (`id`);\
\
--\
-- Indici per le tabelle `arco`\
--\
ALTER TABLE `arco`\
  ADD PRIMARY KEY (`edgeUid`);\
\
--\
-- Indici per le tabelle `AttrDef`\
--\
ALTER TABLE `AttrDef`\
  ADD PRIMARY KEY (`attrDefUid`);\
\
--\
-- Indici per le tabelle `EdgeAttrUsage`\
--\
ALTER TABLE `EdgeAttrUsage`\
  ADD PRIMARY KEY (`edgeAttrUid`);\
\
--\
-- Indici per le tabelle `VertexAttrUsage`\
--\
ALTER TABLE `VertexAttrUsage`\
  ADD PRIMARY KEY (`vertexUsageUid`);\
\
--\
-- Indici per le tabelle `vertice`\
--\
ALTER TABLE `vertice`\
  ADD PRIMARY KEY (`vertexUid`);\
\
--\
-- AUTO_INCREMENT per le tabelle scaricate\
--\
\
--\
-- AUTO_INCREMENT per la tabella `albero`\
--\
ALTER TABLE `albero`\
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;\
--\
-- AUTO_INCREMENT per la tabella `arco`\
--\
ALTER TABLE `arco`\
  MODIFY `edgeUid` int(11) NOT NULL AUTO_INCREMENT;\
--\
-- AUTO_INCREMENT per la tabella `AttrDef`\
--\
ALTER TABLE `AttrDef`\
  MODIFY `attrDefUid` int(11) NOT NULL AUTO_INCREMENT;\
--\
-- AUTO_INCREMENT per la tabella `EdgeAttrUsage`\
--\
ALTER TABLE `EdgeAttrUsage`\
  MODIFY `edgeAttrUid` int(11) NOT NULL AUTO_INCREMENT;\
--\
-- AUTO_INCREMENT per la tabella `VertexAttrUsage`\
--\
ALTER TABLE `VertexAttrUsage`\
  MODIFY `vertexUsageUid` int(11) NOT NULL AUTO_INCREMENT;\
--\
-- AUTO_INCREMENT per la tabella `vertice`\
--\
ALTER TABLE `vertice`\
  MODIFY `vertexUid` int(11) NOT NULL AUTO_INCREMENT;}