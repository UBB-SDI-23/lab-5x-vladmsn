import React from 'react'
import { useEffect, useState } from 'react';
import axios from 'axios';
import { Container, Row, Col, Button, ListGroup } from 'react-bootstrap';

import { useParams } from 'react-router-dom';

const GroupDetailScreen = ({ match }) => {
  const [group, setGroup] = useState(null);
  const { id } = useParams();
  const userId = 5;
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isParticipant, setIsParticipant] = useState(false);
  const maxParticipantsToShow = 5;

  useEffect(() => {
    const fetchGroupDetails = async () => {
      try {
        const response = await axios.get(`/api/v1/groups/${id}`);
        setGroup(response.data);

       (response.data.participants.find(participant => participant.id === userId)) ? setIsParticipant(true) 
                                                                                    : setIsParticipant(false);

      } catch (error) {
        console.log(error);
      }
    };

    fetchGroupDetails();
  }, []);

  if (!group) {
    return <p>Loading...</p>;
  }

  const participantsToShow = group.participants?.slice(0, maxParticipantsToShow) || [];
  const remainingParticipantsCount = Math.max(group.participants?.length - maxParticipantsToShow, 0);
  
  const getInitials = (firstName, lastName) => {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`;
  };

  const addParticipant = async () => {
    try {
      const response = await axios.post(`/api/v1/groups/participant?groupId=${id}&userId=${userId}`);
      setIsParticipant(true);
    } catch (error) {
      console.log(error);
    }
  };

  const removeParticipant = async () => {
    try {
      const response = await axios.delete(`/api/v1/groups/participant?groupId=${id}&userId=${userId}`);
      setIsParticipant(false);
    } catch (error) {
      console.log(error);
    }
  };
  
  return (
    <Container>
      <Row className="my-3">
        <Col sm={12} md={8}>
          <h1>{group.name}</h1>
          <h5>{group.description}</h5>
          {isParticipant ? 
            (<Button className="my-3" onClick={removeParticipant}>Leave Group</Button>)
          : (<Button className="my-3" onClick={addParticipant}>Join Group</Button>)}
          <span className="mx-2"></span>
          <Button className="my-3">Edit</Button>
        </Col>
        <Col sm={12} md={4} className="text-md-right">
          <h2>Participants</h2>
          <ListGroup className="custom-listgroup">
            {participantsToShow.map((participant) => (
              <ListGroup.Item key={participant.id} className="d-flex align-items-center justify-content-between">
                <div className="user-icon">{participant.firstName.charAt(0)}{participant.lastName.charAt(0)}</div>
                <span>{participant.firstName} {participant.lastName}</span>
              </ListGroup.Item>
            ))}
            {remainingParticipantsCount > 0 && (
              <ListGroup.Item variant="secondary" className="text-center">
                + {remainingParticipantsCount} more
              </ListGroup.Item>
            )}
          </ListGroup>
        </Col>
      </Row>
    </Container>
  );
};

export default GroupDetailScreen;