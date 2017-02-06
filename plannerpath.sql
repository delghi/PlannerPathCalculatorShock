SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


CREATE TABLE `albero` (
  `id` int(11) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `splitsize` int(15) DEFAULT NULL,
  `depth` int(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `arco` (
  `edgeUid` int(11) NOT NULL,
  `valore` int(15) NOT NULL,
  `id_albero` int(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `attr_def` (
  `attrDefUid` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `nomeAlbero` varchar(255) NOT NULL,
  `id_albero` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `edge_attr_usage` (
  `edgeAttrUid` int(11) NOT NULL,
  `objectEdgeUid` int(11) NOT NULL,
  `attrDefUid` int(11) NOT NULL,
  `value` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `vertex_attr_usage` (
  `vertexUsageUid` int(11) NOT NULL,
  `objectVid` int(11) NOT NULL,
  `attrDefId` int(11) NOT NULL,
  `value` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `vertice` (
  `vertexUid` int(11) NOT NULL,
  `id_albero` int(15) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `arco_entrante` int(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `albero`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

ALTER TABLE `arco`
  ADD PRIMARY KEY (`edgeUid`),
  ADD KEY `id_albero` (`id_albero`);

ALTER TABLE `attr_def`
  ADD PRIMARY KEY (`attrDefUid`),
  ADD KEY `nomeAlbero` (`nomeAlbero`),
  ADD KEY `id_albero` (`id_albero`);

ALTER TABLE `edge_attr_usage`
  ADD PRIMARY KEY (`edgeAttrUid`),
  ADD KEY `objectEdgeUid` (`objectEdgeUid`),
  ADD KEY `attrDefUid` (`attrDefUid`);

ALTER TABLE `vertex_attr_usage`
  ADD PRIMARY KEY (`vertexUsageUid`),
  ADD KEY `attrDefId` (`attrDefId`),
  ADD KEY `objectVid` (`objectVid`);

ALTER TABLE `vertice`
  ADD PRIMARY KEY (`vertexUid`),
  ADD KEY `id_albero` (`id_albero`),
  ADD KEY `arco_entrante` (`arco_entrante`);


ALTER TABLE `albero`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `arco`
  MODIFY `edgeUid` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `attr_def`
  MODIFY `attrDefUid` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `edge_attr_usage`
  MODIFY `edgeAttrUid` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `vertex_attr_usage`
  MODIFY `vertexUsageUid` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `vertice`
  MODIFY `vertexUid` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `arco`
  ADD CONSTRAINT `alber` FOREIGN KEY (`id_albero`) REFERENCES `albero` (`id`) ON DELETE CASCADE;

ALTER TABLE `attr_def`
  ADD CONSTRAINT `tree` FOREIGN KEY (`id_albero`) REFERENCES `albero` (`id`) ON DELETE CASCADE;

ALTER TABLE `edge_attr_usage`
  ADD CONSTRAINT `attr-edge` FOREIGN KEY (`attrDefUid`) REFERENCES `attr_def` (`attrDefUid`) ON DELETE CASCADE,
  ADD CONSTRAINT `edge-attr-u` FOREIGN KEY (`objectEdgeUid`) REFERENCES `arco` (`edgeUid`) ON DELETE CASCADE;

ALTER TABLE `vertex_attr_usage`
  ADD CONSTRAINT `attr-v` FOREIGN KEY (`attrDefId`) REFERENCES `attr_def` (`attrDefUid`) ON DELETE CASCADE,
  ADD CONSTRAINT `vertex-attr-u` FOREIGN KEY (`objectVid`) REFERENCES `vertice` (`vertexUid`) ON DELETE CASCADE;

ALTER TABLE `vertice`
  ADD CONSTRAINT `arco-en` FOREIGN KEY (`arco_entrante`) REFERENCES `arco` (`edgeUid`) ON DELETE CASCADE,
  ADD CONSTRAINT `albero` FOREIGN KEY (`id_albero`) REFERENCES `albero` (`id`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
