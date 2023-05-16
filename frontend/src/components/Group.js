import React from "react";
import { useState } from 'react';

import { Card } from "react-bootstrap";
import { Link } from "react-router-dom";

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';


const Group = ({ group }) => {

  const [isHovered, setIsHovered] = useState(false);

  const handleMouseEnter = () => {
    setIsHovered(true);
  };

  const handleMouseLeave = () => {
    setIsHovered(false);
  };

  return (
    
    <Card className={`my-3 p-3 rounded`}
              onMouseEnter={handleMouseEnter}
              onMouseLeave={handleMouseLeave}>
      <Link to={`/group/${group.id}`} className="card-link">
      <Card.Body className="d-flex align-items-center">
          <div className="card-content">
            <Card.Title as="h4">
              <strong>{group.name}</strong>
            </Card.Title>
            <Card.Text as="h6">{group.description}</Card.Text>
          </div>
          <div className="flex-grow-1"></div>
          <div className={`card-arrow ${isHovered ? 'show' : ''}`}>
            <FontAwesomeIcon icon={faChevronRight} className="arrow-icon" />
          </div>
        </Card.Body>
      </Link>
    </Card>
  );
};

export default Group;
